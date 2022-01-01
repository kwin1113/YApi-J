package xyz.kwin.yapi.constant;

import com.google.common.collect.Sets;

import java.util.Set;

/**
 * YApi类型映射
 *
 * @author kwin
 * @since 2021/12/26 9:50 下午
 */
public enum YApiResEnum {
    STRING("string", Sets.newHashSet("char", "Character", "String")),
    NUMBER("number", Sets.newHashSet("float", "Float", "double", "Double", "BigDecimal")),
    ARRAY("array", Sets.newHashSet("ArrayDeque", "ArrayList", "Collection", "Deque", "HashSet",
            "LinkedHashSet", "LinkedList", "List", "PriorityQueue", "Queue", "Set", "SortedSet", "Stack", "TreeSet")),
    OBJECT("object", Sets.newHashSet("Map", "HashMap", "Hashtable", "LinkedHashMap", "SortedMap", "TreeMap")),
    BOOLEAN("boolean", Sets.newHashSet("boolean", "Boolean")),
    INTEGER("integer", Sets.newHashSet("short", "Short", "byte", "Byte", "int", "Integer", "BigInteger", "long", "Long")),
    NULL("null", Sets.newHashSet("void", "Void")),
    ;

    private final String type;
    private final Set<String> set;

    YApiResEnum(String type, Set<String> set) {
        this.type = type;
        this.set = set;
    }

    public String getType() {
        return type;
    }

    public Set<String> getSet() {
        return set;
    }

    public static YApiResEnum byType(String type) {
        for (YApiResEnum resEnum : values()) {
            if (resEnum.getType().equals(type)) {
                return resEnum;
            }
        }
        return OBJECT;
    }

    public static YApiResEnum convert(String javaType) {
        for (YApiResEnum resEnum : values()) {
            if (resEnum.getSet().contains(javaType)) {
                return resEnum;
            }
        }
        return OBJECT;
    }
}
