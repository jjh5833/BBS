package bbs.member.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bbs.auth.model.User;
import bbs.auth.service.HashService;
import bbs.member.service.SetNewPasswordService;
import bbs.member.service.YesOrNoService;
import bbs.mvc.command.CommandHandler;

public class SetNewPasswordHandler extends CommandHandler { // 유저가 등록한 인증키가 들어왔다 나가는 곳

	private SetNewPasswordService newPasswordService = new SetNewPasswordService();

	@Override
	protected String getFormViewName() {
		return "/WEB-INF/view/setNewOne.jsp";
	}

	@Override
	protected String processSubmit(HttpServletRequest req, HttpServletResponse res) throws Exception {
		User user = (User) req.getSession().getAttribute("tempAuthUser");

		if (req.getParameter("newPw") != null) {
			String userId = user.getId();
			// String newPw = req.getParameter("newPw");

			String tempnewPw = req.getParameter("newPw");
			String prehexnewPw = HashService.stringToHex(tempnewPw);
			byte[] hexnewPw = HashService.hexStringToByteArray(prehexnewPw);
//			MemberDao dao = new MemberDao();
//			String salt = dao.getSaltById(userId);
			String salt = HashService.setSalt(userId);
			String newPw = HashService.Hashing(hexnewPw, salt);

//			System.out.println(tempnewPw);
//			System.out.println(newPw);

			newPasswordService.setNewPassword(userId, newPw, salt);
			req.getSession().invalidate();
			res.sendRedirect("./boxOffice/list.do?fpwvalue=done");
			return null;
		}

		YesOrNoService fin = new YesOrNoService();

		String userKey = req.getParameter("pwChangeKey");
		req.getSession().setAttribute("userKey", userKey);

		String adminKey = (String) req.getSession().getAttribute("AuthenticationKey");
		String answer = fin.IsSame(adminKey, userKey);

		// System.out.println(adminKey);

		if (answer == "yes") {
			res.sendRedirect("./boxOffice/list.do?fpwvalue=pass");
			return null;
		}

		return getFormViewName();
	}

}
