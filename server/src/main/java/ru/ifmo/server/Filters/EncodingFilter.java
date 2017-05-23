package ru.ifmo.server.Filters;

import ru.ifmo.server.Request;
import ru.ifmo.server.Response;

public class EncodingFilter extends Filter {

    String code = request.getBody().getCharSet();
    public EncodingFilter() {
    }

    public EncodingFilter(Request request, Response response) {
        this.request = request;
        this.response = response;
    }


    @Override
    public void init() {
        this.active = true;
    }


    @Override
    void doFilter(Request request, Response response) {
        init();


      String requestCharSet = request.getBody().getCharSet();

      if(requestCharSet!=null && !(requestCharSet ).equalsIgnoreCase(code)){
         request.setCharSet();
         response.setCharSet();
      }


        if (nextFilter != null)
            nextFilter.doFilter(request, response);
    }

    @Override
    public void setNextFilter(Filter nextFilter) {
        this.nextFilter = nextFilter;
    }

    @Override
    void destroy() {
        this.active = false;
    }
}
