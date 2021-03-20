package com.chacha.common.router;

/**
 * @author chacha
 * @project AwesomeAndroidApp
 * @package_name com.chacha.common.router
 * @date 2/18/21
 * @time 4:55 PM
 */
public class RouterFragmentPath {
    // 首页组件
    public static class Home {
        private static final String HOME = "/home";

        // 首页
        public static final String PAGER_HOME = HOME + "/Home";

    }

    // 社区组件
    public static class Community {
        private static final String COMMUNITY = "/community";

        // 社区页
        public static final String PAGER_COMMUNITY = COMMUNITY + "/Community";
    }

    // 更多组件
    public static class More {
        private static final String MORE = "/more";

        // 更多页面
        public static final String PAGER_MORE = MORE + "/More";
    }

    public static class User {
        private static final String USER = "/user";

        // 个人中心
        public static final String PAGER_USER = USER + "/User";
    }
}
