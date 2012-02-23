package controllers;

import play.*;
import play.mvc.*;

import java.util.*;

import models.*;

public class Application extends Controller {

	public static void index() {
		List<NoteRow> noterows = NoteRow.find("order by id desc").fetch();
		String cssStr = createCSSNameStr(noterows, "");
		String cssStr2 = createCSSNameStr(noterows, " li");
		render(noterows, cssStr, cssStr2);
	}

	public static String createCSSNameStr(List<NoteRow> noterows, String ext) {
		String tmp = "";
		for (NoteRow noteRow : noterows) {
			tmp += "#sortable" + String.valueOf(noteRow.getId().intValue()) + ext + ",";
			System.out.println(tmp);
		}
		System.out.println("return: " + tmp.substring(0, tmp.length() - 1));
		return tmp.substring(0, tmp.length() - 1);
	}

}