package redis.demo;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import redis.clients.jedis.Jedis;
import com.google.gson.Gson;

public class RedisTest {
	public static class DataModel {
		private int cid = 0;
		private List map = new ArrayList();
		public List getMap() {
		return map;
		}
		public void setMap(List map) {
		this.map = map;
		}
	}

	public static class TTMap {
		private int tid;
		private long tm;

		public long getTm() {
			return tm;
		}
		public void setTm(long tm) {
			this.tm = tm;
		}
		public TTMap(int tid, long tm) {
			this.tid = tid;
			this.tm = tm;
		}
	}

//	public static void main(String[] args) throws UnsupportedEncodingException {
        public static void execute(int threadNo) throws UnsupportedEncodingException {
		long pCnt = 500000L; //2500000L;
		int tCnt = 4;
		Gson gson = new Gson();

		long t1,t2,t3,t4,t5;

		Map<String, String> sttg = new HashMap<>((int) pCnt);
		Map<Long, DataModel> rsttg = new HashMap<>();

		long tm = System.currentTimeMillis() / 1000;

		t1 = System.currentTimeMillis();

		for (long p=100001569758007822L; p<100001569758007822L+pCnt; p++) {
			DataModel d = new DataModel();
			for (int t=180000; t<180000+tCnt; t++) {
				d.getMap().add(new TTMap(t, tm + t));
			}

			sttg.put(String.valueOf(p), gson.toJson(d, DataModel.class));
		}

		t2 = System.currentTimeMillis();
		System.out.println(String.format("Thread:%d generate %d items to sttg use %d ms", threadNo, pCnt, t2 - t1));
                
                RedisPool.getInstance().connect();
		Jedis jedis = null;
		try {
			jedis = RedisInstance.getJedis();
//                        jedis = RedisPool.getInstance().getJedis();
			System.out.println(jedis.hmset("RedisTest", sttg));

			t3 = System.currentTimeMillis();
			System.out.println(String.format("write %d items from sttg use %d ms", sttg.size(), t3 - t2));

			Map<String, String> mm = jedis.hgetAll("RedisTest");

			t4 = System.currentTimeMillis();
			System.out.println(String.format("read %d items from sttg use %d ms", mm.size(), t4 - t3));

			for (Map.Entry<String, String> entry : mm.entrySet()) {
				rsttg.put(Long.valueOf(entry.getKey()), gson.fromJson(entry.getValue(), DataModel.class));
			}

			t5 = System.currentTimeMillis();
			System.out.println(String.format("Thread:%d load %d items to rsttg use %d ms", threadNo, mm.size(), t5 - t4));

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			RedisPool.returnResource(jedis);
		}
	}

}
