package com.chacha.common.router;

/**
 * @author chacha
 * @project AwesomeAndroidApp
 * @package_name com.chacha.common.router
 * @date 2/18/21
 * @time 4:32 PM
 *
 * <p>
 *     组件化Activity路径配置，利用ARouter进行Activity跳转的统一路径注册
 * </p>
 *
 */
public class RouterActivityPath {
    // Main组件
    public static class Main {
        private static final String MAIN = "/main";

        // 主页面
        public static final String PAGER_MAIN = MAIN + "/Main";
    }

    // 视频播放(video)组件
    public static class Video {
        private static final String VIDEO = "/video";

        // 视频播放界面
        public static final String PAGER_VIDEO = VIDEO + "/Video";
    }

    // User组件
    public static class User {
        private static final String USER = "/user";

        // 登录界面
        public static final String PAGER_LOGIN = USER + "/Login";

        // 关注页面
        public static final String PAGER_ATTENTION = USER + "/attention";
    }
}
