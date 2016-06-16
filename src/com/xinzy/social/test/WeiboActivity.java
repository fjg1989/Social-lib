package com.xinzy.social.test;

import com.xinzy.social.SocialException;
import com.xinzy.social.oauth.Oauth;
import com.xinzy.social.oauth.OauthCallback;
import com.xinzy.social.oauth.U;
import com.xinzy.social.share.Entry;
import com.xinzy.social.share.Share;
import com.xinzy.social.share.ShareCallback;
import com.xinzy.social.test.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class WeiboActivity extends Activity implements OnClickListener {

	private TextView mTextView;

	private static final int[] IDS = { R.id.login, R.id.share, };

	private Oauth mOauth;
	private Share mShare;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_weibo);
		setTitle("Weibo");

		mTextView = (TextView) findViewById(R.id.infoText);
		for (int id : IDS) {
			findViewById(id).setOnClickListener(this);
		}
		mOauth = Oauth.getInstance(this);
		mOauth.setCallback(new OauthBack());

		mShare = Share.getInstance(this);
		mShare.setCallback(new ShareBack());
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		mShare.handleWeiboResponse(intent);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.login:
			mOauth.onWeibo();
			break;

		case R.id.share:
			String path = Environment.getExternalStorageDirectory().getAbsolutePath();
			Entry entry = new Entry().setTitle("测试分享到各大平台").setContent("这里是分享的内容，啦啦啦啦啦啦啦啊啦啦啦啦啦啦")
					.setUrl("http://androidweekly.cn/").addImage(path + "/TEST/imgs/1.jpg");
			mShare.weibo(entry);
			break;

		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		mOauth.onActivityResult(requestCode, resultCode, data);
	}

	class ShareBack implements ShareCallback {

		@Override
		public void onSuccess(int platform) {
			mTextView.append("onSuccess " + platform);
		}

		@Override
		public void onFailure(int platform, String msg) {
			mTextView.append("onFailure " + platform + "; msg = " + msg);
		}

		@Override
		public void onCancelled(int platform) {
			mTextView.append("onCancelled " + platform);
		}
		
	}

	class OauthBack implements OauthCallback {

		@Override
		public void onSuccess(int platform, U user) {
			mTextView.setText("onSuccess: " + platform + " " + user);
		}

		@Override
		public void onFailure(int platform, String msg) {

			mTextView.setText("onFailure: " + platform + " " + msg);
		}

		@Override
		public void onCancelled(int platform) {

			mTextView.setText("onCancelled: " + platform);
		}

		@Override
		public void onError(int platform, SocialException ex) {

			L.e("onError: " + platform, ex);
		}
	}
}
