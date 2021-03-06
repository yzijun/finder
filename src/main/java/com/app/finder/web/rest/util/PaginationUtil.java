package com.app.finder.web.rest.util;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Utility class for handling pagination.
 *
 * <p>
 * Pagination uses the same principles as the <a href="https://developer.github.com/v3/#pagination">Github API</api>,
 * and follow <a href="http://tools.ietf.org/html/rfc5988">RFC 5988 (Link header)</a>.
 * </p>
 */
public class PaginationUtil {

    public static HttpHeaders generatePaginationHttpHeaders(Page<?> page, String baseUrl)
        throws URISyntaxException {

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Total-Count", "" + page.getTotalElements());
        // 向HttpHeaders中新增加当前页的设定值
        headers.add("X-Page-Number", "" + page.getNumber());
        // 向HttpHeaders中新增加总页数的设定值
        headers.add("X-Page-TotalPages", "" + page.getTotalPages());
        String link = "";
        if ((page.getNumber() + 1) < page.getTotalPages()) {
            link = "<" + (new URI(baseUrl +"?page=" + (page.getNumber() + 1) + "&size=" + page.getSize())).toString() + ">; rel=\"next\",";
        }
        // prev link
        if ((page.getNumber()) > 0) {
            link += "<" + (new URI(baseUrl +"?page=" + (page.getNumber() - 1) + "&size=" + page.getSize())).toString() + ">; rel=\"prev\",";
        }
        // last and first link
        int lastPage = 0;
        if (page.getTotalPages() > 0) {
            lastPage = page.getTotalPages() - 1;
        }
        link += "<" + (new URI(baseUrl +"?page=" + lastPage + "&size=" + page.getSize())).toString() + ">; rel=\"last\",";
        link += "<" + (new URI(baseUrl +"?page=" + 0 + "&size=" + page.getSize())).toString() + ">; rel=\"first\"";
        headers.add(HttpHeaders.LINK, link);
        return headers;
    }
}
