package leyramu.framework.lersosa.common.core.utils.html;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * HTML过滤器，用于去除XSS漏洞隐患
 *
 * @author <a href="mailto:2038322151@qq.com">Miraitowa_zcx</a>
 * @version 1.0.0
 * @since 2024/10/19
 */
public final class HTMLFilter {

    /**
     * 在 PHP 中表示 /si 修饰符的正则表达式标志联合
     */
    private static final int REGEX_FLAGS_SI = Pattern.CASE_INSENSITIVE | Pattern.DOTALL;
    private static final Pattern P_COMMENTS = Pattern.compile("<!--(.*?)-->", Pattern.DOTALL);
    private static final Pattern P_COMMENT = Pattern.compile("^!--(.*)--$", REGEX_FLAGS_SI);
    private static final Pattern P_TAGS = Pattern.compile("<(.*?)>", Pattern.DOTALL);
    private static final Pattern P_END_TAG = Pattern.compile("^/([a-z0-9]+)", REGEX_FLAGS_SI);
    private static final Pattern P_START_TAG = Pattern.compile("^([a-z0-9]+)(.*?)(/?)$", REGEX_FLAGS_SI);
    private static final Pattern P_QUOTED_ATTRIBUTES = Pattern.compile("([a-z0-9]+)=([\"'])(.*?)\\2", REGEX_FLAGS_SI);
    private static final Pattern P_UNQUOTED_ATTRIBUTES = Pattern.compile("([a-z0-9]+)(=)([^\"\\s']+)", REGEX_FLAGS_SI);
    private static final Pattern P_PROTOCOL = Pattern.compile("^([^:]+):", REGEX_FLAGS_SI);
    private static final Pattern P_ENTITY = Pattern.compile("&#(\\d+);?");
    private static final Pattern P_ENTITY_UNICODE = Pattern.compile("&#x([0-9a-f]+);?");
    private static final Pattern P_ENCODE = Pattern.compile("%([0-9a-f]{2});?");
    private static final Pattern P_VALID_ENTITIES = Pattern.compile("&([^&;]*)(?=(;|&|$))");
    private static final Pattern P_VALID_QUOTES = Pattern.compile("(>|^)([^<]+?)(<|$)", Pattern.DOTALL);
    private static final Pattern P_END_ARROW = Pattern.compile("^>");
    private static final Pattern P_BODY_TO_END = Pattern.compile("<([^>]*?)(?=<|$)");
    private static final Pattern P_XML_CONTENT = Pattern.compile("(^|>)([^<]*?)(?=>)");
    private static final Pattern P_STRAY_LEFT_ARROW = Pattern.compile("<([^>]*?)(?=<|$)");
    private static final Pattern P_STRAY_RIGHT_ARROW = Pattern.compile("(^|>)([^<]*?)(?=>)");
    private static final Pattern P_AMP = Pattern.compile("&");
    private static final Pattern P_QUOTE = Pattern.compile("\"");
    private static final Pattern P_LEFT_ARROW = Pattern.compile("<");
    private static final Pattern P_RIGHT_ARROW = Pattern.compile(">");
    private static final Pattern P_BOTH_ARROWS = Pattern.compile("<>");

    private static final ConcurrentMap<String, Pattern> P_REMOVE_PAIR_BLANKS = new ConcurrentHashMap<>();
    private static final ConcurrentMap<String, Pattern> P_REMOVE_SELF_BLANKS = new ConcurrentHashMap<>();

    /**
     * 允许的 HTML 元素集，以及每个元素允许的属性
     */
    private final Map<String, List<String>> vAllowed;

    /**
     * 每个（允许的）HTML 元素的打开标记计数
     */
    private final Map<String, Integer> vTagCounts = new HashMap<>();

    /**
     * 必须始终是自闭合的 HTML 元素（例如 “<img />”）
     */
    private final String[] vSelfClosingTags;

    /**
     * HTML 元素，这些元素必须始终具有单独的开始和结束标签（例如 “<b></b>”）
     */
    private final String[] vNeedClosingTags;

    /**
     * 不允许的 HTML 元素集
     */
    private final String[] vDisallowed;

    /**
     * 应检查有效协议的属性
     */
    private final String[] vProtocolAtts;

    /**
     * 允许的协议
     */
    private final String[] vAllowedProtocols;

    /**
     * 如果标签不包含任何内容，则应将其删除（例如 “<b></b>” 或 “<b />”）
     */
    private final String[] vRemoveBlanks;

    /**
     * HTML 标记中允许的实体
     */
    private final String[] vAllowedEntities;

    /**
     * 标志确定是否允许在输入 String 中注释
     */
    private final boolean stripComment;

    /**
     * 标志确定是否应编码双引号（“”）和单引号（‘’’），以使它们在 HTML 属性中安全使用
     */
    private final boolean encodeQuotes;

    /**
     * 确定在显示“不平衡”尖括号（例如 “<b text </b>”
     * 变为 “<b> 文本 </b>”）。如果设置为 false，则不平衡的尖括号将被 html 转义
     */
    @Getter
    private final boolean alwaysMakeTags;

    /**
     * 默认构造函数
     */
    public HTMLFilter() {
        vAllowed = new HashMap<>();

        final ArrayList<String> aAtts = new ArrayList<>();
        aAtts.add("href");
        aAtts.add("target");
        vAllowed.put("a", aAtts);

        final ArrayList<String> imgAtts = new ArrayList<>();
        imgAtts.add("src");
        imgAtts.add("width");
        imgAtts.add("height");
        imgAtts.add("alt");
        vAllowed.put("img", imgAtts);

        final ArrayList<String> noAtts = new ArrayList<>();
        vAllowed.put("b", noAtts);
        vAllowed.put("strong", noAtts);
        vAllowed.put("i", noAtts);
        vAllowed.put("em", noAtts);

        vSelfClosingTags = new String[]{"img"};
        vNeedClosingTags = new String[]{"a", "b", "strong", "i", "em"};
        vDisallowed = new String[]{};
        vAllowedProtocols = new String[]{"http", "mailto", "https"};
        vProtocolAtts = new String[]{"src", "href"};
        vRemoveBlanks = new String[]{"a", "b", "strong", "i", "em"};
        vAllowedEntities = new String[]{"amp", "gt", "lt", "quot"};
        stripComment = true;
        encodeQuotes = true;
        alwaysMakeTags = false;
    }

    /**
     * Map-parameter 可配置构造函数
     *
     * @param conf map 中包含配置，键匹配字段名称
     */
    @SuppressWarnings("unchecked")
    public HTMLFilter(final Map<String, Object> conf) {

        assert conf.containsKey("vAllowed") : "configuration requires vAllowed";
        assert conf.containsKey("vSelfClosingTags") : "configuration requires vSelfClosingTags";
        assert conf.containsKey("vNeedClosingTags") : "configuration requires vNeedClosingTags";
        assert conf.containsKey("vDisallowed") : "configuration requires vDisallowed";
        assert conf.containsKey("vAllowedProtocols") : "configuration requires vAllowedProtocols";
        assert conf.containsKey("vProtocolAtts") : "configuration requires vProtocolAtts";
        assert conf.containsKey("vRemoveBlanks") : "configuration requires vRemoveBlanks";
        assert conf.containsKey("vAllowedEntities") : "configuration requires vAllowedEntities";

        vAllowed = Collections.unmodifiableMap((HashMap<String, List<String>>) conf.get("vAllowed"));
        vSelfClosingTags = (String[]) conf.get("vSelfClosingTags");
        vNeedClosingTags = (String[]) conf.get("vNeedClosingTags");
        vDisallowed = (String[]) conf.get("vDisallowed");
        vAllowedProtocols = (String[]) conf.get("vAllowedProtocols");
        vProtocolAtts = (String[]) conf.get("vProtocolAtts");
        vRemoveBlanks = (String[]) conf.get("vRemoveBlanks");
        vAllowedEntities = (String[]) conf.get("vAllowedEntities");
        stripComment = conf.containsKey("stripComment") ? (Boolean) conf.get("stripComment") : true;
        encodeQuotes = conf.containsKey("encodeQuotes") ? (Boolean) conf.get("encodeQuotes") : true;
        alwaysMakeTags = conf.containsKey("alwaysMakeTags") ? (Boolean) conf.get("alwaysMakeTags") : true;
    }

    /**
     * 清理 HTML 过滤器
     */
    private void reset() {
        vTagCounts.clear();
    }

    /**
     * 16进制数字字符集
     *
     * @param decimal 16进制数字
     * @return 字符串
     */
    public static String chr(final int decimal) {
        return String.valueOf((char) decimal);
    }

    /**
     * HTML 实体编码：将包含 HTML 实体的文本进行编码，防止 XSS 攻击
     *
     * @param s 可能包含 HTML 实体的文本
     * @return 不包含 HTML 实体的文本
     */
    public static String htmlSpecialChars(final String s) {
        String result = s;
        result = regexReplace(P_AMP, "&amp;", result);
        result = regexReplace(P_QUOTE, "&quot;", result);
        result = regexReplace(P_LEFT_ARROW, "&lt;", result);
        result = regexReplace(P_RIGHT_ARROW, "&gt;", result);
        return result;
    }

    /**
     * 给定用户提交的 input String，过滤掉任何无效或受限制的 html
     *
     * @param input 文本（即由用户提交）可能包含 HTML
     * @return 输入的“干净”版本，只允许有效的白名单 HTML 元素
     */
    public String filter(final String input) {
        reset();

        String s = input;

        s = escapeComments(s);
        s = balanceHtml(s);
        s = checkTags(s);
        s = processRemoveBlanks(s);

        return s;
    }

    /**
     * 获取过滤器是否删除注释
     *
     * @return 过滤器是否删除注释
     */
    public boolean isStripComments() {
        return stripComment;
    }

    /**
     * 删除 HTML 注释
     *
     * @param s HTML 文本
     * @return 删除注释后的文本
     */
    private String escapeComments(final String s) {
        final Matcher m = P_COMMENTS.matcher(s);
        final StringBuilder buf = new StringBuilder();
        if (m.find()) {
            final String match = m.group(1);
            m.appendReplacement(buf, Matcher.quoteReplacement("<!--" + htmlSpecialChars(match) + "-->"));
        }
        m.appendTail(buf);

        return buf.toString();
    }

    /**
     * 尝试使用 HTML 4.0 DTD 验证给定的 HTML，并尝试修复错误
     *
     * @param s HTML 文本
     * @return 验证、修复后的文本
     */
    private String balanceHtml(String s) {
        if (alwaysMakeTags) {
            s = regexReplace(P_END_ARROW, "", s);
            s = regexReplace(P_BODY_TO_END, "<$1>", s);
            s = regexReplace(P_XML_CONTENT, "$1<$2", s);

        } else {
            s = regexReplace(P_STRAY_LEFT_ARROW, "&lt;$1", s);
            s = regexReplace(P_STRAY_RIGHT_ARROW, "$1$2&gt;<", s);
            s = regexReplace(P_BOTH_ARROWS, "", s);
        }

        return s;
    }

    /**
     * 验证给定标签是否允许，并尝试修复错误
     *
     * @param s HTML 文本
     * @return 修复后的文本
     */
    private String checkTags(String s) {
        Matcher m = P_TAGS.matcher(s);

        final StringBuilder buf = new StringBuilder();
        while (m.find()) {
            String replaceStr = m.group(1);
            replaceStr = processTag(replaceStr);
            m.appendReplacement(buf, Matcher.quoteReplacement(replaceStr));
        }
        m.appendTail(buf);

        final StringBuilder sBuilder = new StringBuilder(buf.toString());
        for (String key : vTagCounts.keySet()) {
            for (int ii = 0; ii < vTagCounts.get(key); ii++) {
                sBuilder.append("</").append(key).append(">");
            }
        }
        s = sBuilder.toString();

        return s;
    }

    /**
     * 验证属性，并尝试修复错误
     *
     * @param s HTML 文本
     * @return 修复后的文本
     */
    private String processRemoveBlanks(final String s) {
        String result = s;
        for (String tag : vRemoveBlanks) {
            if (!P_REMOVE_PAIR_BLANKS.containsKey(tag)) {
                P_REMOVE_PAIR_BLANKS.putIfAbsent(tag, Pattern.compile("<" + tag + "(\\s[^>]*)?></" + tag + ">"));
            }
            result = regexReplace(P_REMOVE_PAIR_BLANKS.get(tag), "", result);
            if (!P_REMOVE_SELF_BLANKS.containsKey(tag)) {
                P_REMOVE_SELF_BLANKS.putIfAbsent(tag, Pattern.compile("<" + tag + "(\\s[^>]*)?/>"));
            }
            result = regexReplace(P_REMOVE_SELF_BLANKS.get(tag), "", result);
        }

        return result;
    }

    /**
     * 正则表达式匹配
     *
     * @param regexPattern 正则表达式模式
     * @param replacement  替换字符串
     * @param s            要匹配的字符串
     * @return 替换后的字符串
     */
    private static String regexReplace(final Pattern regexPattern, final String replacement, final String s) {
        Matcher m = regexPattern.matcher(s);
        return m.replaceAll(replacement);
    }

    private String processTag(final String s) {
        Matcher m = P_END_TAG.matcher(s);
        if (m.find()) {
            final String name = m.group(1).toLowerCase();
            if (allowed(name)) {
                if (!inArray(name, vSelfClosingTags)) {
                    if (vTagCounts.containsKey(name)) {
                        vTagCounts.put(name, vTagCounts.get(name) - 1);
                        return "</" + name + ">";
                    }
                }
            }
        }

        m = P_START_TAG.matcher(s);
        if (m.find()) {
            final String name = m.group(1).toLowerCase();
            final String body = m.group(2);
            String ending = m.group(3);

            if (allowed(name)) {
                final StringBuilder params = new StringBuilder();

                final Matcher m2 = P_QUOTED_ATTRIBUTES.matcher(body);
                final Matcher m3 = P_UNQUOTED_ATTRIBUTES.matcher(body);
                final List<String> paramNames = new ArrayList<>();
                final List<String> paramValues = new ArrayList<>();
                while (m2.find()) {
                    paramNames.add(m2.group(1));
                    paramValues.add(m2.group(3));
                }
                while (m3.find()) {
                    paramNames.add(m3.group(1));
                    paramValues.add(m3.group(3));
                }

                String paramName, paramValue;
                for (int ii = 0; ii < paramNames.size(); ii++) {
                    paramName = paramNames.get(ii).toLowerCase();
                    paramValue = paramValues.get(ii);

                    if (allowedAttribute(name, paramName)) {
                        if (inArray(paramName, vProtocolAtts)) {
                            paramValue = processParamProtocol(paramValue);
                        }
                        params.append(' ').append(paramName).append("=\\\"").append(paramValue).append("\\\"");
                    }
                }

                if (inArray(name, vSelfClosingTags)) {
                    ending = " /";
                }

                if (inArray(name, vNeedClosingTags)) {
                    ending = "";
                }

                if (ending == null || ending.isEmpty()) {
                    if (vTagCounts.containsKey(name)) {
                        vTagCounts.put(name, vTagCounts.get(name) + 1);
                    } else {
                        vTagCounts.put(name, 1);
                    }
                } else {
                    ending = " /";
                }
                return "<" + name + params + ending + ">";
            } else {
                return "";
            }
        }

        m = P_COMMENT.matcher(s);
        if (!stripComment && m.find()) {
            return "<" + m.group() + ">";
        }

        return "";
    }

    /**
     * 验证协议，并尝试修复错误
     *
     * @param s HTML 文本
     * @return 修复后的文本
     */
    private String processParamProtocol(String s) {
        s = decodeEntities(s);
        final Matcher m = P_PROTOCOL.matcher(s);
        if (m.find()) {
            final String protocol = m.group(1);
            if (!inArray(protocol, vAllowedProtocols)) {
                s = "#" + s.substring(protocol.length() + 1);
                if (s.startsWith("#//")) {
                    s = "#" + s.substring(3);
                }
            }
        }

        return s;
    }

    /**
     * 验证实体，并尝试修复错误
     *
     * @param s HTML 文本
     * @return 修复后的文本
     */
    private String decodeEntities(String s) {
        StringBuilder buf = new StringBuilder();

        Matcher m = P_ENTITY.matcher(s);
        while (m.find()) {
            final String match = m.group(1);
            final int decimal = Integer.decode(match);
            m.appendReplacement(buf, Matcher.quoteReplacement(chr(decimal)));
        }
        m.appendTail(buf);
        s = buf.toString();

        buf = new StringBuilder();
        m = P_ENTITY_UNICODE.matcher(s);
        s = getString(buf, m);

        buf = new StringBuilder();
        m = P_ENCODE.matcher(s);
        s = getString(buf, m);

        s = validateEntities(s);
        return s;
    }

    /**
     * 获取字符串
     *
     * @param buf StringBuilder
     * @param m   Matcher
     * @return String
     */
    @NotNull
    private String getString(StringBuilder buf, Matcher m) {
        String s;
        while (m.find()) {
            final String match = m.group(1);
            final int decimal = Integer.valueOf(match, 16);
            m.appendReplacement(buf, Matcher.quoteReplacement(chr(decimal)));
        }
        m.appendTail(buf);
        s = buf.toString();
        return s;
    }

    /**
     * 验证实体
     *
     * @param s HTML 文本
     * @return 修复后的文本
     */
    private String validateEntities(final String s) {
        StringBuilder buf = new StringBuilder();

        Matcher m = P_VALID_ENTITIES.matcher(s);
        while (m.find()) {
            final String one = m.group(1);
            final String two = m.group(2);
            m.appendReplacement(buf, Matcher.quoteReplacement(checkEntity(one, two)));
        }
        m.appendTail(buf);

        return encodeQuotes(buf.toString());
    }

    /**
     * 验证引号
     *
     * @param s HTML 文本
     * @return 修复后的文本
     */
    private String encodeQuotes(final String s) {
        if (encodeQuotes) {
            StringBuilder buf = new StringBuilder();
            Matcher m = P_VALID_QUOTES.matcher(s);
            while (m.find()) {
                final String one = m.group(1);
                final String two = m.group(2);
                final String three = m.group(3);
                m.appendReplacement(buf, Matcher.quoteReplacement(one + two + three));
            }
            m.appendTail(buf);
            return buf.toString();
        } else {
            return s;
        }
    }

    /**
     * 验证实体
     *
     * @param preamble 前缀
     * @return String
     */
    private String checkEntity(final String preamble, final String term) {
        return ";".equals(term) && isValidEntity(preamble) ? '&' + preamble : "&amp;" + preamble;
    }

    /**
     * 验证实体
     *
     * @param entity HTML 文本
     * @return boolean
     */
    private boolean isValidEntity(final String entity) {
        return inArray(entity, vAllowedEntities);
    }

    /**
     * 验证数组
     *
     * @param s     HTML 文本
     * @param array 数组
     * @return boolean
     */
    private static boolean inArray(final String s, final String[] array) {
        for (String item : array) {
            if (item != null && item.equals(s)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 验证是否允许
     *
     * @param name HTML 文本
     * @return boolean
     */
    private boolean allowed(final String name) {
        return (vAllowed.isEmpty() || vAllowed.containsKey(name)) && !inArray(name, vDisallowed);
    }

    /**
     * 验证是否允许属性
     *
     * @param name HTML 文本
     * @return booleano
     */
    private boolean allowedAttribute(final String name, final String paramName) {
        return allowed(name) && (vAllowed.isEmpty() || vAllowed.get(name).contains(paramName));
    }
}
