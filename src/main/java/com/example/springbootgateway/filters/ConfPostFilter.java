package com.example.springbootgateway.filters;


import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.NettyWriteResponseFilter;
import org.springframework.cloud.gateway.filter.factory.rewrite.ModifyResponseBodyGatewayFilterFactory;
import org.springframework.cloud.gateway.filter.factory.rewrite.RewriteFunction;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;


@Component
public class ConfPostFilter implements GlobalFilter, Ordered {
    @Autowired
    private ModifyResponseBodyGatewayFilterFactory modifyResponseBodyGatewayFilterFactory;
    @Autowired
    private BodyRewrite bodyRewrite;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        GatewayFilter delegate=modifyResponseBodyGatewayFilterFactory.apply(new ModifyResponseBodyGatewayFilterFactory.Config()
                .setRewriteFunction(byte[].class, byte[].class, bodyRewrite));

        return delegate.filter(exchange, chain).then(Mono.fromRunnable(()-> {
            System.out.println("Global Post Filter (Conf) executed");
        }));
    }

    @Override
    public int getOrder() {
        return NettyWriteResponseFilter.WRITE_RESPONSE_FILTER_ORDER-1;
    }
}

@Component
class BodyRewrite implements RewriteFunction<byte[], byte[]> {
    @Override
    public Publisher<byte[]> apply(ServerWebExchange exchange, byte[] body) {
        String originalBody = body==null?"":new String(body);

        System.out.println("Body: " + originalBody);
        /*
        if (!ServerWebExchangeUtils.isAlreadyRouted(exchange)) {
            return Mono.just(originalBody.getBytes());
        } else {
            // its the reponse body when already routed
            throw new RuntimeException("The response body is already routed");
        }
        */
        return Mono.just(originalBody.getBytes());
    }
}