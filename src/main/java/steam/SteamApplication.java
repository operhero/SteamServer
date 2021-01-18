package steam;

import com.alibaba.fastjson.JSONArray;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import steam.dao.CDKeyMapper;
import steam.dao.UserMapper;
import steam.domain.CDKey;
import steam.domain.User;

import javax.servlet.http.HttpServletRequest;
import java.security.SecureRandom;
import java.util.*;

@Controller
@SpringBootApplication
@MapperScan("steam.dao")
public class SteamApplication {
    @Autowired
    private UserMapper userMapper;
	@Autowired
	private CDKeyMapper cdKeyMapper;

	public static void main(String[] args) {
		SpringApplication.run(SteamApplication.class, args);
	}

	@RequestMapping(value = "/login", produces = "text/javascript;charset=UTF-8")
	public @ResponseBody
	String getServerLists(@RequestParam(value = "steamid") long steamid,
						  @RequestParam(value = "serverKey") String serverKey, HttpServletRequest request)  {
		String key = steamid + "_" + serverKey;
        User user = userMapper.selectByPrimaryKey(key);
		if(user == null)
		{
			user = new User();
			user.setUid(key);
			userMapper.insert(user);
		}
		return key;
	}

	@RequestMapping(value = "/profile", produces = "text/javascript;charset=UTF-8")
	public @ResponseBody
	String getServerLists(@RequestParam(value = "sid") String sid,
						  HttpServletRequest request) {
        User user = userMapper.selectByPrimaryKey(sid);
		Object obj = JSONArray.toJSON(user);
		String json = obj.toString();
		return json;
	}

	@RequestMapping(value = "/battle-end", produces = "text/javascript;charset=UTF-8")
	public @ResponseBody
	String getServerLists(@RequestParam(value = "sid") String sid,
						  @RequestParam(value = "data") String data,
						  HttpServletRequest request)  {
		User user = userMapper.selectByPrimaryKey(sid);
		user.setGamedata(data);
		userMapper.updateByPrimaryKeySelective(user);
		return "success";
	}

	@RequestMapping(value = "/gen-cdkey", produces = "text/javascript;charset=UTF-8")
	public @ResponseBody
	String genCDKey(@RequestParam(value = "productid") int productid,
					@RequestParam(value = "len") int len,
					@RequestParam(value = "num") int num,
					HttpServletRequest request)  {
		char[] chars = new char[]{'a','b','c','d','e','f','g','h','j','k','m','n','p','q','r','s','t','u','v','w','x','y','z',
				'1','2','3','4','5','6','7','8','9'};
		int cnt = chars.length;
		Random rd = new SecureRandom();
		List<String> ret = new ArrayList<String>(num);
		while (ret.size()<num) {
			StringBuilder sb = new StringBuilder(len);
			for (int i = 0; i < len; i++) {
				sb.append(chars[rd.nextInt(cnt - 1)]);
			}
			String key = sb.toString();
			try {
				CDKey cdKey = new CDKey();
				cdKey.setCdkey(key);
				cdKey.setProductId(productid);
				cdKey.setState(0);
				cdKeyMapper.insertSelective(cdKey);
				ret.add(key);
			} catch (Exception e){
				System.out.println(e.toString());
			}
		}

		Object obj = JSONArray.toJSON(ret);
		String json = obj.toString();
		return json;
	}

	@RequestMapping(value = "/use-cdkey", produces = "text/javascript;charset=UTF-8")
	public @ResponseBody
	String useCDKey(@RequestParam(value = "key") String key,
					@RequestParam(value = "steamid") String steamid,
					HttpServletRequest request)  {
		Map<String, String> ret = new HashMap<String,String>();
		CDKey cdKey = cdKeyMapper.selectByPrimaryKey(key);
		if(cdKey == null){
			ret.put("code","1");
			ret.put("msg","兑换码不存在");
			ret.put("productid","0");
		}else if(cdKey.getState() == 1){
			ret.put("code","2");
			ret.put("msg","兑换码已使用");
			ret.put("productid","0");
		}
		else{
			cdKey.setState(1);
			cdKey.setUsedSteamId(steamid);
			cdKeyMapper.updateByPrimaryKeySelective(cdKey);
			ret.put("code","0");
			ret.put("msg","使用成功");
			ret.put("productid",cdKey.getProductId().toString());
		}

		Object obj = JSONArray.toJSON(ret);
		String json = obj.toString();
		return json;
	}
}
