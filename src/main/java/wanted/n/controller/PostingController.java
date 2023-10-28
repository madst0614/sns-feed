package wanted.n.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/posting")
@Api(tags="Posting API", description="포스팅과 관련된 API")
@RestController
public class PostingController {
}
