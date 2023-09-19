# Endpoint URI Tagging for Prometheus Metrics in Spring Boot

## Problem Description

When using Spring Boot with Prometheus for monitoring and metrics, you might encounter a situation where Prometheus registers requests with an "UNKNOWN" URI. This happens because, by default, Spring Boot does not provide Prometheus with endpoint information, leading to incomplete monitoring data.

![Problem Screenshot](/images/problem-screenshot.png)

## Solution

To resolve this issue and obtain accurate endpoint information in Prometheus, we can implement a URI tagging filter. This filter will capture incoming requests, extract their URIs, and tag the Prometheus metrics with the corresponding URIs. 

## Implementation

In your Spring Boot application, follow these steps:

1. Create a controller that handles a generic route for various URIs:
   
   ```java
   @RestController
   @RequestMapping("/report/odata4")
   public class TestController {
       @RequestMapping("/**")
       @GetMapping
       @PostMapping
       public void genericRoute(final HttpServletRequest req, final HttpServletResponse resp) throws IOException {
           resp.setContentType("text/html");
           try (final PrintWriter writer = resp.getWriter()) {
               writer.println("<html><body>");
               writer.println("<h1>Hello, Controller</h1>");
               writer.println("</body></html>");
           }
       }
   }


2. Create a configuration class that registers a UriTaggingFilter as a filter bean. This filter will tag Prometheus metrics with the request URI.
    ```java
    @Configuration
    public class PrometheusUriFilterConfig {
        @Bean
        public FilterRegistrationBean<UriTaggingFilter> uriTaggingFilter(MeterRegistry meterRegistry) {
            FilterRegistrationBean<UriTaggingFilter> registrationBean = new FilterRegistrationBean<>();
            registrationBean.setFilter(new UriTaggingFilter(meterRegistry));
            registrationBean.addUrlPatterns("/*");
            return registrationBean;
        }

        @WebFilter("/*")
        public static class UriTaggingFilter extends OncePerRequestFilter {
            private final MeterRegistry meterRegistry;

            public UriTaggingFilter(MeterRegistry meterRegistry) {
                this.meterRegistry = meterRegistry;
            }

            @Override
            protected void doFilterInternal(
                    HttpServletRequest request,
                    HttpServletResponse response,
                    FilterChain filterChain) throws ServletException, IOException {

                long startTime = System.currentTimeMillis();

                try {
                    filterChain.doFilter(request, response);
                } finally {
                    long endTime = System.currentTimeMillis();
                    long requestTime = endTime - startTime;

                    String uri = request.getRequestURI();

                    Timer.builder("http.server.requests")
                            .tags("uri", uri)
                            .register(meterRegistry)
                            .record(requestTime, TimeUnit.MILLISECONDS);
                }
            }
        }
    }
    ```

With this configuration, your Prometheus metrics will now include accurate endpoint information.

![Problem Screenshot](/images/problem-solution.png)