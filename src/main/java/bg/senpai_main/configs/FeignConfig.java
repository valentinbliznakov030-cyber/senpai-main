package bg.senpai_main.configs;
import feign.codec.Decoder;
import feign.optionals.OptionalDecoder;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;


@Configuration
public class FeignConfig {

    @Bean
    public Decoder feignDecoder() {
        return new OptionalDecoder(
                new ResponseEntityDecoder(
                        new SpringDecoder(() -> new HttpMessageConverters(new RestTemplate().getMessageConverters()))
                )
        );
    }
}
