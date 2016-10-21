package com.walkindeep.teammanagerpreview;

/**
 * Created by Guo Jiahao on 2016/10/21.
 */

import com.walkindeep.teammanagerpreview.Project.User;

import java.util.*;
import java.io.*;
import java.security.*;

import static android.R.id.message;

/**
 * 用来获取用户的头像，以便在需要显示用户头像的地方显示。
 */
public class UserAvatarGetter {
    private static String hex(byte[] array) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < array.length; ++i) {
            sb.append(Integer.toHexString((array[i]
                    & 0xFF) | 0x100).substring(1, 3));
        }
        return sb.toString();
    }


    public static String getAvatarURL(User user) {
        try {
            MessageDigest md =
                    MessageDigest.getInstance("MD5");
            return "https://www.gravatar.com/avatar/" +
                    hex(md.digest(user.getUsername().getBytes("CP1252")));
        } catch (NoSuchAlgorithmException e) {
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
}