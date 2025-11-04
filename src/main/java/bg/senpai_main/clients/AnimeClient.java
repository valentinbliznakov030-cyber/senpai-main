package bg.senpai_main.clients;

import bg.senpai.common.dtos.AnimeM3U8LinkDto;
import bg.senpai_main.configs.FeignConfig;
import bg.senpai_main.dtos.AnimeStreamRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(
        name = "anime-service",
        url = "http://localhost:8081/api/v1/anime", // <-- смени порта ако твоя е различен
        configuration = FeignConfig.class
)
public interface AnimeClient {

    @GetMapping("/m3u8Link")
    AnimeM3U8LinkDto getM3u8Link(@RequestParam("url") String animeUrl);

    @PostMapping(value = "/stream", consumes = "application/json")
    ResponseEntity<Resource> streamAnime(@RequestParam("m3u8Link") String m3u8Link, @RequestParam("vidName") String vidName);
}

