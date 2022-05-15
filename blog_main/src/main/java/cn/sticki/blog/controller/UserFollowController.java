package cn.sticki.blog.controller;

import cn.sticki.blog.pojo.domain.FansBasic;
import cn.sticki.blog.pojo.domain.FollowBasic;
import cn.sticki.blog.pojo.domain.User;
import cn.sticki.blog.pojo.vo.ListVO;
import cn.sticki.blog.pojo.vo.RestTemplate;
import cn.sticki.blog.security.AuthenticationFacade;
import cn.sticki.blog.service.UserFollowService;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 用户关注接口
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserFollowController {

	// 默认每页20条
	private final int pageSize = 20;

	@Resource
	private AuthenticationFacade authenticationFacade;

	@Resource
	private UserFollowService userFollowService;

	/**
	 * 获取关注列表
	 *
	 * @param page 当前页
	 * @return 关注列表数据
	 */
	@GetMapping("/follow")
	public RestTemplate<ListVO<FollowBasic>> getFollowList(@RequestParam(defaultValue = "1") int page) {
		User user = authenticationFacade.getUser();
		ListVO<FollowBasic> listVO = userFollowService.getFollowList(user.getId(), page, pageSize);
		return new RestTemplate<>(listVO);
	}

	/**
	 * 获取粉丝列表
	 *
	 * @param page 当前页
	 * @return 粉丝列表数据
	 */
	@GetMapping("/fans")
	public RestTemplate<ListVO<FansBasic>> getFansList(@RequestParam(defaultValue = "1") int page) {
		User user = authenticationFacade.getUser();
		ListVO<FansBasic> listVO = userFollowService.getFansList(user.getId(), page, pageSize);
		return new RestTemplate<>(listVO);
	}

	/**
	 * 关注其他用户
	 *
	 * @param followId 被关注的用户id
	 * @return 关注状态，若成功则为true，取关则为false
	 */
	@PostMapping("/follow")
	public RestTemplate doFollow(@NotNull Integer followId) {
		User user = authenticationFacade.getUser();
		try {
			boolean result = userFollowService.follow(user.getId(), followId);
			return new RestTemplate(200, "success", result, true);
		} catch (Exception e) {
			return new RestTemplate(200, "fail", null, false);
		}
	}

}
