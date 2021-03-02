package steam;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.codec.digest.DigestUtils;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import steam.service.RedisTempleService;
import steam.dao.CDKeyMapper;
import steam.dao.UserMapper;
import steam.domain.CDKey;
import steam.domain.UserWithBLOBs;

import javax.servlet.http.HttpServletRequest;
import java.security.SecureRandom;
import java.util.*;

@Controller
@SpringBootApplication
@MapperScan("steam.dao")
@EnableAspectJAutoProxy
@ComponentScan(basePackages={"steam.service","steam.filter"})
public class SteamApplication {
    @Autowired
    private UserMapper userMapper;
	@Autowired
	private CDKeyMapper cdKeyMapper;

	private static String serverKey;
	@Value("${serverKey}")
	public void setServerKey(String key){
		serverKey = key;
	}

	@Autowired
	private RedisTempleService redisTempleService;

	private String endlessRankName = "endlessRank";

	public static void main(String[] args) {
		SpringApplication.run(SteamApplication.class, args);
	}

	@RequestMapping(value = "/login", produces = "text/javascript;charset=UTF-8")
	public @ResponseBody
	String login(@RequestParam(value = "steamid") String steamid,
						  @RequestParam(value = "sign") String sign,
						  HttpServletRequest request)  {
		StringBuffer md5buf = new StringBuffer();
		md5buf.append("steamid=").append(steamid).append("&serverKey=").append(serverKey);
		String _sign = DigestUtils.md5Hex(md5buf.toString());
		if (!sign.equalsIgnoreCase(_sign)){
			return "sign error";
		}

		UserWithBLOBs user = userMapper.selectByPrimaryKey(steamid + "");
		if(user == null)
		{
			user = new UserWithBLOBs();
			user.setUid(steamid);
			userMapper.insert(user);
		}
		return steamid + "";
	}

	@RequestMapping(value = "/profile", produces = "text/javascript;charset=UTF-8")
	public @ResponseBody
	String profile(@RequestParam(value = "steamid") String steamid,
						  @RequestParam(value = "sign") String sign,
						  HttpServletRequest request) {
		StringBuffer md5buf = new StringBuffer();
		md5buf.append("steamid=").append(steamid).append("&serverKey=").append(serverKey);
		String _sign = DigestUtils.md5Hex(md5buf.toString());
		if (!sign.equalsIgnoreCase(_sign)){
			return "sign error";
		}

		UserWithBLOBs user = userMapper.selectByPrimaryKey(steamid);
		JSONObject object = new JSONObject();
		object.put("uid",user.getUid());
		object.put("gamedata",user.getGamedata());
		String json = object.toString();
		return json;
	}

	@RequestMapping(value = "/battle-end", produces = "text/javascript;charset=UTF-8")
	public @ResponseBody
	String battleEnd(@RequestParam(value = "steamid") String steamid,
						  @RequestParam(value = "data") String data,
						  @RequestParam(value = "sign") String sign,
						  HttpServletRequest request)  {
		StringBuffer md5buf = new StringBuffer();
		md5buf.append("steamid=").append(steamid).append("&data=").append(data).append("&serverKey=").append(serverKey);
		String _sign = DigestUtils.md5Hex(md5buf.toString());
		if (!sign.equalsIgnoreCase(_sign)){
			return "sign error";
		}
		UserWithBLOBs user = userMapper.selectByPrimaryKey(steamid);

		JSONObject json = JSONObject.parseObject(user.getGamedata());
		int s = json!=null && json.getInteger("score")!=null?json.getInteger("score").intValue():0;
		int e = json!=null && json.getInteger("endless")!=null?json.getInteger("endless").intValue():0;
		json = JSONObject.parseObject(data);
		int ts = json.getIntValue("score");
		int te = json.getIntValue("endless");

		s+=ts;
		if(te>e)
		{
			e = te;
			redisTempleService.add(endlessRankName,steamid,e);
		}

		JSONObject object = new JSONObject();
		object.put("score",s);
		object.put("endless",e);
		user.setGamedata(object.toString());
		userMapper.updateByPrimaryKeySelective(user);
		return "success";
	}

	@RequestMapping(value = "/depot-profile", produces = "text/javascript;charset=UTF-8")
	public @ResponseBody
	String depotProfile(@RequestParam(value = "steamid") String steamid,
						  @RequestParam(value = "sign") String sign,
						  HttpServletRequest request)  {
		StringBuffer md5buf = new StringBuffer();
		md5buf.append("steamid=").append(steamid).append("&serverKey=").append(serverKey);
		String _sign = DigestUtils.md5Hex(md5buf.toString());
		if (!sign.equalsIgnoreCase(_sign)){
			return "sign error";
		}

		UserWithBLOBs user = userMapper.selectByPrimaryKey(steamid);
		JSONObject object = new JSONObject();
		object.put("uid",user.getUid());
		object.put("depot",user.getDepot());
		String json = object.toString();
		return json;
	}

	@RequestMapping(value = "/depot-update", produces = "text/javascript;charset=UTF-8")
	public @ResponseBody
	String depotUpdate(@RequestParam(value = "steamid") String steamid,
					   @RequestParam(value = "depot") String depot,
					   @RequestParam(value = "sign") String sign,
					   HttpServletRequest request)  {
		StringBuffer md5buf = new StringBuffer();
		md5buf.append("steamid=").append(steamid).append("&depot=").append(depot).append("&serverKey=").append(serverKey);
		String _sign = DigestUtils.md5Hex(md5buf.toString());
		if (!sign.equalsIgnoreCase(_sign)){
			return "sign error";
		}

			UserWithBLOBs user = userMapper.selectByPrimaryKey(steamid);
			user.setDepot(depot);
			userMapper.updateByPrimaryKeySelective(user);


		return "success";
	}

	@RequestMapping(value = "/load-shop-history", produces = "text/javascript;charset=UTF-8")
	public @ResponseBody
	String loadShopHistory(@RequestParam(value = "steamid") String steamid,
						@RequestParam(value = "sign") String sign,
						HttpServletRequest request)  {
		StringBuffer md5buf = new StringBuffer();
		md5buf.append("steamid=").append(steamid).append("&serverKey=").append(serverKey);
		String _sign = DigestUtils.md5Hex(md5buf.toString());
		if (!sign.equalsIgnoreCase(_sign)){
			return "sign error";
		}

		UserWithBLOBs user = userMapper.selectByPrimaryKey(steamid);
		JSONObject object = new JSONObject();
		object.put("uid",user.getUid());
		object.put("shop_data",user.getShopData());
		String json = object.toString();
		return json;
	}

	@RequestMapping(value = "/save-shop-history", produces = "text/javascript;charset=UTF-8")
	public @ResponseBody
	String saveShopHistory(@RequestParam(value = "steamid") String steamid,
					   @RequestParam(value = "shop_data") String shop_data,
					   @RequestParam(value = "sign") String sign,
					   HttpServletRequest request)  {
		StringBuffer md5buf = new StringBuffer();
		md5buf.append("steamid=").append(steamid).append("&shop_data=").append(shop_data).append("&serverKey=").append(serverKey);
		String _sign = DigestUtils.md5Hex(md5buf.toString());
		if (!sign.equalsIgnoreCase(_sign)){
			return "sign error";
		}

		UserWithBLOBs user = userMapper.selectByPrimaryKey(steamid);
		user.setShopData(shop_data);
		userMapper.updateByPrimaryKeySelective(user);
		return "success";
	}

	@RequestMapping(value = "/endless-rank", produces = "text/javascript;charset=UTF-8")
	public @ResponseBody
	String getEndlessRank(@RequestParam(value = "steamid") String steamid,
						   HttpServletRequest request)  {
		Set<ZSetOperations.TypedTuple<String>> rank = redisTempleService.revRangeWithScore(endlessRankName,0,99);
		Long myRank = redisTempleService.revrank(endlessRankName,steamid);
		Double myScore = redisTempleService.score(endlessRankName,steamid);
		StringBuilder sb = new StringBuilder(101);
		int r = 1;
		for (ZSetOperations.TypedTuple<String> tuple:rank) {
			sb.append(tuple.getValue()).append(',').append(r++).append(',').append(tuple.getScore().intValue()).append(';');
		}


		if(myRank != null && myScore !=null){
			sb.append(steamid).append(',').append(myRank + 1).append(',').append(myScore.intValue());
		}else{
			sb.append(steamid).append(",-1,-1");
		}

		return sb.toString();
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
				CDKey cdKey = new CDKey();
				cdKey.setCdkey(key);
				cdKey.setProductId(productid);
				cdKey.setState(0);
				cdKeyMapper.insertSelective(cdKey);
				ret.add(key);
		}

		Object obj = JSONArray.toJSON(ret);
		String json = obj.toString();
		return json;
	}

	@RequestMapping(value = "/use-cdkey", produces = "text/javascript;charset=UTF-8")
	public @ResponseBody
	String useCDKey(@RequestParam(value = "key") String key,
					@RequestParam(value = "steamid") String steamid,
					@RequestParam(value = "sign") String sign,
					HttpServletRequest request)  {
		StringBuffer md5buf = new StringBuffer();
		md5buf.append("key=").append(key).append("&steamid=").append(steamid).append("&serverKey=").append(serverKey);
		String _sign = DigestUtils.md5Hex(md5buf.toString());
		if (!sign.equalsIgnoreCase(_sign)){
			return "sign error";
		}

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
