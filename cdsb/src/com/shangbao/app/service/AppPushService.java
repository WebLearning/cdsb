package com.shangbao.app.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class AppPushService {
	
	private RestTemplate restTemplate;
	private String url = "https://api.jpush.cn/v3/push";
	private String localhost = "http://www.cdsb.mobi/cdsb";
	private String appKey;
	private String masterSecret;
	private String encoded;
	
	@Autowired
	public AppPushService(@Qualifier("restTemplate") RestTemplate restTemplateT) {
		Properties props = new Properties();
		try {
			props=PropertiesLoaderUtils.loadAllProperties("config.properties");
			appKey = props.getProperty("push_appKey");
			masterSecret = props.getProperty("push_masterSecret");
			url = props.getProperty("push_URL");
			localhost = props.getProperty("localhost");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		restTemplate = restTemplateT;
		endcode();
		addHeader("Authorization", "Basic " + encoded);
	}
	
	public void push1(String message, long newsId){
		String result = "";
		try {
			result = restTemplate.postForObject(url, getJson(message, newsId), String.class);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(result);
	}
	
	public String push(String message, long newsId){
		DefaultHttpClient httpclient = new DefaultHttpClient();
		HttpPost post = new HttpPost(url);
		String returnMessage = "";
		try {
			StringEntity s = new StringEntity(getJson(message, newsId), HTTP.UTF_8);
//			System.out.println(s);
			s.setContentEncoding("utf-8");
			s.setContentType("application/json");
			post.addHeader("Authorization", "Basic " + encoded);
			post.setEntity(s);
			HttpResponse response = httpclient.execute(post);
			JSONObject object = new JSONObject(EntityUtils.toString(response.getEntity()));
			if(object.has("error")){
				JSONObject error = object.getJSONObject("error");
				if(error.has("code")){
					int code = error.getInt("code");
					if(code == 1005){
						returnMessage = "超过推送长度限制";
					}else{
						returnMessage = "错误代码" + code + ",请查看极光推送文档";
					}
				}
			}else{
				returnMessage = "推送成功";
			}
//			System.out.println(EntityUtils.toString(response.getEntity()));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return returnMessage;
	}
	
	private String getJson(String alert, long newsId){
		String json = "";
		Map<String, Object> map = new HashMap<>();
		map.put("platform", "all");
		map.put("audience", "all");
		
		Map<String, Object> notification = new HashMap<>();
		Map<String, Object> ios = new HashMap<>();
		Map<String, Object> android = new HashMap<>();
		Map<String, Object> extras = new HashMap<>();
		Map<String, Object> options = new HashMap<>();
		
		ios.put("alert", alert);
		android.put("alert", alert);
		extras.put("newsId", newsId);
		extras.put("ID", newsId);
		extras.put("url", localhost + "/app/ios/articledetail/" + newsId);
		ios.put("extras", extras);
		android.put("extras", extras);
		notification.put("ios", ios);
		notification.put("android", android);
		options.put("apns_production", true);
		
		map.put("notification", notification);
		map.put("options", options);
		ObjectMapper mapper = new ObjectMapper();
		try {
			json = mapper.writeValueAsString(map);
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		System.out.println(json);
		return json;
	}
	
	private void addHeader(final String headerName, final String headerValue){
		List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
		interceptors.add(new ClientHttpRequestInterceptor() {
			
			@Override
			public ClientHttpResponse intercept(HttpRequest request, byte[] body,
					ClientHttpRequestExecution execution) throws IOException {
				request.getHeaders().add(headerName, headerValue);
				return execution.execute(request, body);
			}
		});
		restTemplate.setInterceptors(interceptors);
	}
	
	private void endcode(){
		if(appKey != null && masterSecret != null){
			encoded = new String(Base64.encodeBase64((appKey + ":" + masterSecret).getBytes()));
		}
	}
	
	public RestTemplate getRestTemplate() {
		return restTemplate;
	}

	public void setRestTemplate(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getAppKey() {
		return appKey;
	}

	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}

	public String getMasterSecret() {
		return masterSecret;
	}

	public void setMasterSecret(String masterSecret) {
		this.masterSecret = masterSecret;
	}

	public String getEncoded() {
		return encoded;
	}

	public void setEncoded(String encoded) {
		this.encoded = encoded;
	}
	
}
