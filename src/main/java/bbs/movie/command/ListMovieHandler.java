package bbs.movie.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bbs.logic.page.Page;
import bbs.logic.service.PageListService;
import bbs.movie.dao.MovieDao;
import bbs.movie.model.Movie;
import bbs.mvc.command.CommandHandler;

public class ListMovieHandler extends CommandHandler {

	private PageListService<Movie> listService;
	
	public ListMovieHandler() {
		String tableName = "movie";
		String orderRule = "open_dt DESC";
		MovieDao<Movie> dao = new MovieDao<Movie>(tableName, orderRule);
		
		listService = new PageListService<Movie>(dao);
	}
	
	@Override
	protected String getFormViewName() {
		return "/WEB-INF/view/listMovie.jsp";
	}

	@Override
	protected String processForm(HttpServletRequest req, HttpServletResponse res) throws Exception {
		String pageNoVal = req.getParameter("pageNo");
		int pageNo = 1;
		if (pageNoVal != null) {
			pageNo = Integer.parseInt(pageNoVal);
		}
		
		String condition = "open_dt <= curdate()";
		Page<Movie> page = listService.getPage(pageNo, condition);
		req.setAttribute("page", page);
		
		return getFormViewName();
	}
	
	@Override
	protected String processSubmit(HttpServletRequest req, HttpServletResponse res) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
}
