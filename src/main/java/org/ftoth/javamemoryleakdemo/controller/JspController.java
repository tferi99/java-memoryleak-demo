package org.ftoth.javamemoryleakdemo.controller;

import org.ftoth.javamemoryleakdemo.exception.ValidationError;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class JspController
{
	public static final String CHECKBOX_CHECKED = "checked";

	protected String getRequiredStringParam(String paramName, String value)
		throws ValidationError
	{
		if (value == null || value.trim().equals("")) {
			throw new ValidationError("'" + paramName + "' required");
		}
		return value;
	}

	protected int getIntParam(String paramName, String value)
		throws ValidationError
	{
		int val;
		try {
			return Integer.parseInt(value);
		}
		catch (NumberFormatException e) {
			throw new ValidationError("'" + paramName + "': bad format");
		}
		catch (Exception e) {
			throw new ValidationError("'" + paramName + "': " + e.getMessage());
		}
	}

	protected int getRequiredIntParam(String paramName, String value)
		throws ValidationError
	{
		String val = getRequiredStringParam(paramName, value);
		return getIntParam(paramName, val);
	}

	public static String createRefreshHeadCode(HttpServletRequest request, HttpServletResponse response, int refreshSecs)
	{
		StringBuilder b = new StringBuilder();
		int msesc = refreshSecs * 1000;
		String cp = request.getContextPath();

		b.append("\n");
		b.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n");
		b.append("\n");
		b.append("<script src= \"" + cp + "/webjars/jquery/3.3.1/jquery.min.js\" type=\"text/javascript\"></script>\n");		// loaded by caller JSP
		b.append("\n");

		b.append("<script type=\"text/javascript\">\n");
		b.append("var refreshing = true;\n");
		b.append("var refreshSecs = " + refreshSecs + ";\n");
		b.append("function doRefresh() {\n");
		b.append("    if (refreshing) {\n");
		b.append("        refresh();\n");
		b.append("        setTimeout(\"doRefresh()\", " + msesc + ");\n");
		b.append("    }\n");
		b.append("}\n");
		b.append("\n");
		b.append("function refresh() {\n");
		b.append("    location.reload()\n");
		b.append("}\n");
		b.append("\n");
		b.append("function startRefresh() {\n");
		b.append("    refreshing = true;\n");
		b.append("    setTimeout(\"doRefresh()\", " + msesc + ");\n");
		b.append("    $('.btnRefreshStart').hide();\n");
		b.append("    $('.btnRefreshStop').show();\n");
		b.append("}\n");
		b.append("\n");
		b.append("function stopRefresh() {\n");
		b.append("    refreshing = false;\n");
		b.append("    $('.btnRefreshStart').show();\n");
		b.append("    $('.btnRefreshStop').hide();\n");
		b.append("}\n");
		b.append("\n");
		b.append("$(function() {\n");
		b.append("    startRefresh();\n");
		b.append("});\n");
		b.append("</script>\n");

		b.append("\n");
		b.append("<style>\n");
		b.append(".placeholder {\n");
		b.append("    padding-left: 40px; display:inline\n");
		b.append("}\n");
		b.append(".buttonDiv {\n");
		b.append("    display:inline\n");
		b.append("}\n");
		b.append("</style>\n");

		return b.toString();
	}

	public static String createRefreshBodyCode(HttpServletRequest request, HttpServletResponse response, int refreshSecs)
	{
		StringBuilder b = new StringBuilder();

		b.append("<form>\n");
		/*
		 * b.append(
		 * "    <button type=\"button\" onclick=\"refresh()\">REFRESH</button>\n"
		 * ); b.append("    <div class=\"placeholder\"></div>\n");
		 */
		b.append("    <div class=\"btnRefreshStart buttonDiv\"><span>&nbsp;</span>\n");
		b.append("        <button type=\"button\" onclick=\"startRefresh()\" class=\"btn btn-success\"><span class=\"glyphicon glyphicon-play\" aria-hidden=\"true\"></span> Start auto-refresh</button>\n");
		b.append("        &nbsp stopped\n");
		b.append("    </div>\n");
		b.append("    <div class=\"btnRefreshStop buttonDiv\"><span>&nbsp;</span>\n");
		b.append("        <button type=\"button\" onclick=\"stopRefresh()\" class=\"btn btn-danger\"><span class=\"glyphicon glyphicon-stop\" aria-hidden=\"true\"></span> Stop auto-refresh</button>\n");
		b.append("        &nbsp Refreshing in every " + refreshSecs + " seconds...\n");
		b.append("    </div>\n");
		b.append("</form>\n");
		b.append("<br/>\n");

		return b.toString();
	}

}
