package com.hrushko.entity;

import java.util.EnumSet;
import java.util.Objects;

public class Permission extends Entity {
    private String name;
    private EnumSet<Rule> rules;

    public Permission() {
        rules = EnumSet.noneOf(Rule.class);
    }

    public Permission(String name) {
        this();
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void addRule(Rule rule) {
        rules.add(rule);
    }

    public EnumSet<Rule> getRules() {
        return rules;
    }

    public boolean checkRule(Rule rule) {
        return rules.contains(rule);
    }

    public void setRules(EnumSet<Rule> rules) {
        this.rules = rules;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Permission that = (Permission) o;
        return Objects.equals(name, that.name) && Objects.equals(rules, that.rules);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name, rules);
    }

    @Override
    public String toString() {
        return "Permission{" +
                "name='" + name + '\'' +
                ", rules=" + rules +
                '}';
    }
}
