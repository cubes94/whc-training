package com.whc.training.api.util.enums;

import java.util.Objects;

/**
 * 区域枚举
 *
 * @author whc
 * @version 1.0.0
 * @since 2018年04月17 10:55
 */
public interface DictAreaEnum {

    /**
     * 级别枚举
     */
    enum Level {

        /**
         * 枚举值
         */
        COUNTRY(0, "国"),
        PROVINCE(1, "省"),
        CITY(2, "市"),
        DISTRICT(3, "区/县");

        /**
         * 代码
         */
        private Integer code;

        /**
         * 名称
         */
        private String name;

        Level(Integer code, String name) {
            this.code = code;
            this.name = name;
        }

        public Integer getCode() {
            return code;
        }

        public String getName() {
            return name;
        }

        /**
         * 通过代码获取名称
         *
         * @param code 代码
         * @return 名称
         */
        public static String getNameByCode(Integer code) {
            Level level = getObjByCode(code);
            return level == null ? "" : level.getName();
        }

        /**
         * 通过名称获取代码
         *
         * @param name 名称
         * @return 代码
         */
        public static Integer getCodeByName(String name) {
            for (Level obj : Level.values()) {
                if (Objects.equals(obj.getName(), name)) {
                    return obj.getCode();
                }
            }
            return null;
        }

        /**
         * 通过代码获取对象
         *
         * @param code 代码
         * @return 对象
         */
        public static Level getObjByCode(Integer code) {
            for (Level obj : Level.values()) {
                if (Objects.equals(obj.getCode(), code)) {
                    return obj;
                }
            }
            return null;
        }

        /**
         * 比code等级低
         *
         * @param code 代码
         * @return 是否比code等级低
         */
        public Boolean lowerThan(Integer code) {
            return this.code > code;
        }

        /**
         * 比level等级低
         *
         * @param level 等级
         * @return 是否比level等级低
         */
        public Boolean lowerThan(Level level) {
            return this.lowerThan(level.getCode());
        }

        /**
         * 获取下一级代码
         *
         * @return 下一级代码
         */
        public Integer getNextLevelCode() {
            return Level.DISTRICT.lowerThan(this.code) ? this.code + 1 : null;
        }

        /**
         * 获取下一级
         *
         * @return 下一级
         */
        public Level getNextLevel() {
            Integer nextLevelCode = this.getNextLevelCode();
            return nextLevelCode == null ? null : getObjByCode(nextLevelCode);
        }
    }


    /**
     * 状态枚举
     */
    enum Status {

        /**
         * 枚举值
         */
        VALID(1, "有效"),
        INVALID(0, "无效"),
        DELETED(-1, "已删除");

        /**
         * 代码
         */
        private Integer code;

        /**
         * 名称
         */
        private String name;

        Status(Integer code, String name) {
            this.code = code;
            this.name = name;
        }

        public Integer getCode() {
            return code;
        }

        public String getName() {
            return name;
        }

        /**
         * 通过代码获取名称
         *
         * @param code 代码
         * @return 名称
         */
        public static String getNameByCode(Integer code) {
            for (Status obj : Status.values()) {
                if (Objects.equals(obj.getCode(), code)) {
                    return obj.getName();
                }
            }
            return "";
        }

        /**
         * 通过名称获取代码
         *
         * @param name 名称
         * @return 代码
         */
        public static Integer getCodeByName(String name) {
            for (Status obj : Status.values()) {
                if (Objects.equals(obj.getName(), name)) {
                    return obj.getCode();
                }
            }
            return null;
        }
    }


}
