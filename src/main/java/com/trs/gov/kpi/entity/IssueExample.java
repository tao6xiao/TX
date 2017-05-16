package com.trs.gov.kpi.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class IssueExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public IssueExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andIdIsNull() {
            addCriterion("id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Integer value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Integer value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Integer value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Integer value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Integer value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Integer> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Integer> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Integer value1, Integer value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Integer value1, Integer value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andSiteIdIsNull() {
            addCriterion("siteId is null");
            return (Criteria) this;
        }

        public Criteria andSiteIdIsNotNull() {
            addCriterion("siteId is not null");
            return (Criteria) this;
        }

        public Criteria andSiteIdEqualTo(Integer value) {
            addCriterion("siteId =", value, "siteId");
            return (Criteria) this;
        }

        public Criteria andSiteIdNotEqualTo(Integer value) {
            addCriterion("siteId <>", value, "siteId");
            return (Criteria) this;
        }

        public Criteria andSiteIdGreaterThan(Integer value) {
            addCriterion("siteId >", value, "siteId");
            return (Criteria) this;
        }

        public Criteria andSiteIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("siteId >=", value, "siteId");
            return (Criteria) this;
        }

        public Criteria andSiteIdLessThan(Integer value) {
            addCriterion("siteId <", value, "siteId");
            return (Criteria) this;
        }

        public Criteria andSiteIdLessThanOrEqualTo(Integer value) {
            addCriterion("siteId <=", value, "siteId");
            return (Criteria) this;
        }

        public Criteria andSiteIdIn(List<Integer> values) {
            addCriterion("siteId in", values, "siteId");
            return (Criteria) this;
        }

        public Criteria andSiteIdNotIn(List<Integer> values) {
            addCriterion("siteId not in", values, "siteId");
            return (Criteria) this;
        }

        public Criteria andSiteIdBetween(Integer value1, Integer value2) {
            addCriterion("siteId between", value1, value2, "siteId");
            return (Criteria) this;
        }

        public Criteria andSiteIdNotBetween(Integer value1, Integer value2) {
            addCriterion("siteId not between", value1, value2, "siteId");
            return (Criteria) this;
        }

        public Criteria andTypeIdIsNull() {
            addCriterion("typeId is null");
            return (Criteria) this;
        }

        public Criteria andTypeIdIsNotNull() {
            addCriterion("typeId is not null");
            return (Criteria) this;
        }

        public Criteria andTypeIdEqualTo(Boolean value) {
            addCriterion("typeId =", value, "typeId");
            return (Criteria) this;
        }

        public Criteria andTypeIdNotEqualTo(Boolean value) {
            addCriterion("typeId <>", value, "typeId");
            return (Criteria) this;
        }

        public Criteria andTypeIdGreaterThan(Boolean value) {
            addCriterion("typeId >", value, "typeId");
            return (Criteria) this;
        }

        public Criteria andTypeIdGreaterThanOrEqualTo(Boolean value) {
            addCriterion("typeId >=", value, "typeId");
            return (Criteria) this;
        }

        public Criteria andTypeIdLessThan(Boolean value) {
            addCriterion("typeId <", value, "typeId");
            return (Criteria) this;
        }

        public Criteria andTypeIdLessThanOrEqualTo(Boolean value) {
            addCriterion("typeId <=", value, "typeId");
            return (Criteria) this;
        }

        public Criteria andTypeIdIn(List<Boolean> values) {
            addCriterion("typeId in", values, "typeId");
            return (Criteria) this;
        }

        public Criteria andTypeIdNotIn(List<Boolean> values) {
            addCriterion("typeId not in", values, "typeId");
            return (Criteria) this;
        }

        public Criteria andTypeIdBetween(Boolean value1, Boolean value2) {
            addCriterion("typeId between", value1, value2, "typeId");
            return (Criteria) this;
        }

        public Criteria andTypeIdNotBetween(Boolean value1, Boolean value2) {
            addCriterion("typeId not between", value1, value2, "typeId");
            return (Criteria) this;
        }

        public Criteria andSubTypeIdIsNull() {
            addCriterion("subTypeId is null");
            return (Criteria) this;
        }

        public Criteria andSubTypeIdIsNotNull() {
            addCriterion("subTypeId is not null");
            return (Criteria) this;
        }

        public Criteria andSubTypeIdEqualTo(Boolean value) {
            addCriterion("subTypeId =", value, "subTypeId");
            return (Criteria) this;
        }

        public Criteria andSubTypeIdNotEqualTo(Boolean value) {
            addCriterion("subTypeId <>", value, "subTypeId");
            return (Criteria) this;
        }

        public Criteria andSubTypeIdGreaterThan(Boolean value) {
            addCriterion("subTypeId >", value, "subTypeId");
            return (Criteria) this;
        }

        public Criteria andSubTypeIdGreaterThanOrEqualTo(Boolean value) {
            addCriterion("subTypeId >=", value, "subTypeId");
            return (Criteria) this;
        }

        public Criteria andSubTypeIdLessThan(Boolean value) {
            addCriterion("subTypeId <", value, "subTypeId");
            return (Criteria) this;
        }

        public Criteria andSubTypeIdLessThanOrEqualTo(Boolean value) {
            addCriterion("subTypeId <=", value, "subTypeId");
            return (Criteria) this;
        }

        public Criteria andSubTypeIdIn(List<Boolean> values) {
            addCriterion("subTypeId in", values, "subTypeId");
            return (Criteria) this;
        }

        public Criteria andSubTypeIdNotIn(List<Boolean> values) {
            addCriterion("subTypeId not in", values, "subTypeId");
            return (Criteria) this;
        }

        public Criteria andSubTypeIdBetween(Boolean value1, Boolean value2) {
            addCriterion("subTypeId between", value1, value2, "subTypeId");
            return (Criteria) this;
        }

        public Criteria andSubTypeIdNotBetween(Boolean value1, Boolean value2) {
            addCriterion("subTypeId not between", value1, value2, "subTypeId");
            return (Criteria) this;
        }

        public Criteria andDetailIsNull() {
            addCriterion("detail is null");
            return (Criteria) this;
        }

        public Criteria andDetailIsNotNull() {
            addCriterion("detail is not null");
            return (Criteria) this;
        }

        public Criteria andDetailEqualTo(String value) {
            addCriterion("detail =", value, "detail");
            return (Criteria) this;
        }

        public Criteria andDetailNotEqualTo(String value) {
            addCriterion("detail <>", value, "detail");
            return (Criteria) this;
        }

        public Criteria andDetailGreaterThan(String value) {
            addCriterion("detail >", value, "detail");
            return (Criteria) this;
        }

        public Criteria andDetailGreaterThanOrEqualTo(String value) {
            addCriterion("detail >=", value, "detail");
            return (Criteria) this;
        }

        public Criteria andDetailLessThan(String value) {
            addCriterion("detail <", value, "detail");
            return (Criteria) this;
        }

        public Criteria andDetailLessThanOrEqualTo(String value) {
            addCriterion("detail <=", value, "detail");
            return (Criteria) this;
        }

        public Criteria andDetailLike(String value) {
            addCriterion("detail like", value, "detail");
            return (Criteria) this;
        }

        public Criteria andDetailNotLike(String value) {
            addCriterion("detail not like", value, "detail");
            return (Criteria) this;
        }

        public Criteria andDetailIn(List<String> values) {
            addCriterion("detail in", values, "detail");
            return (Criteria) this;
        }

        public Criteria andDetailNotIn(List<String> values) {
            addCriterion("detail not in", values, "detail");
            return (Criteria) this;
        }

        public Criteria andDetailBetween(String value1, String value2) {
            addCriterion("detail between", value1, value2, "detail");
            return (Criteria) this;
        }

        public Criteria andDetailNotBetween(String value1, String value2) {
            addCriterion("detail not between", value1, value2, "detail");
            return (Criteria) this;
        }

        public Criteria andIssueTimeIsNull() {
            addCriterion("issueTime is null");
            return (Criteria) this;
        }

        public Criteria andIssueTimeIsNotNull() {
            addCriterion("issueTime is not null");
            return (Criteria) this;
        }

        public Criteria andIssueTimeEqualTo(Date value) {
            addCriterion("issueTime =", value, "issueTime");
            return (Criteria) this;
        }

        public Criteria andIssueTimeNotEqualTo(Date value) {
            addCriterion("issueTime <>", value, "issueTime");
            return (Criteria) this;
        }

        public Criteria andIssueTimeGreaterThan(Date value) {
            addCriterion("issueTime >", value, "issueTime");
            return (Criteria) this;
        }

        public Criteria andIssueTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("issueTime >=", value, "issueTime");
            return (Criteria) this;
        }

        public Criteria andIssueTimeLessThan(Date value) {
            addCriterion("issueTime <", value, "issueTime");
            return (Criteria) this;
        }

        public Criteria andIssueTimeLessThanOrEqualTo(Date value) {
            addCriterion("issueTime <=", value, "issueTime");
            return (Criteria) this;
        }

        public Criteria andIssueTimeIn(List<Date> values) {
            addCriterion("issueTime in", values, "issueTime");
            return (Criteria) this;
        }

        public Criteria andIssueTimeNotIn(List<Date> values) {
            addCriterion("issueTime not in", values, "issueTime");
            return (Criteria) this;
        }

        public Criteria andIssueTimeBetween(Date value1, Date value2) {
            addCriterion("issueTime between", value1, value2, "issueTime");
            return (Criteria) this;
        }

        public Criteria andIssueTimeNotBetween(Date value1, Date value2) {
            addCriterion("issueTime not between", value1, value2, "issueTime");
            return (Criteria) this;
        }

        public Criteria andIsResolvedIsNull() {
            addCriterion("isResolved is null");
            return (Criteria) this;
        }

        public Criteria andIsResolvedIsNotNull() {
            addCriterion("isResolved is not null");
            return (Criteria) this;
        }

        public Criteria andIsResolvedEqualTo(Boolean value) {
            addCriterion("isResolved =", value, "isResolved");
            return (Criteria) this;
        }

        public Criteria andIsResolvedNotEqualTo(Boolean value) {
            addCriterion("isResolved <>", value, "isResolved");
            return (Criteria) this;
        }

        public Criteria andIsResolvedGreaterThan(Boolean value) {
            addCriterion("isResolved >", value, "isResolved");
            return (Criteria) this;
        }

        public Criteria andIsResolvedGreaterThanOrEqualTo(Boolean value) {
            addCriterion("isResolved >=", value, "isResolved");
            return (Criteria) this;
        }

        public Criteria andIsResolvedLessThan(Boolean value) {
            addCriterion("isResolved <", value, "isResolved");
            return (Criteria) this;
        }

        public Criteria andIsResolvedLessThanOrEqualTo(Boolean value) {
            addCriterion("isResolved <=", value, "isResolved");
            return (Criteria) this;
        }

        public Criteria andIsResolvedIn(List<Boolean> values) {
            addCriterion("isResolved in", values, "isResolved");
            return (Criteria) this;
        }

        public Criteria andIsResolvedNotIn(List<Boolean> values) {
            addCriterion("isResolved not in", values, "isResolved");
            return (Criteria) this;
        }

        public Criteria andIsResolvedBetween(Boolean value1, Boolean value2) {
            addCriterion("isResolved between", value1, value2, "isResolved");
            return (Criteria) this;
        }

        public Criteria andIsResolvedNotBetween(Boolean value1, Boolean value2) {
            addCriterion("isResolved not between", value1, value2, "isResolved");
            return (Criteria) this;
        }

        public Criteria andIsDelIsNull() {
            addCriterion("isDel is null");
            return (Criteria) this;
        }

        public Criteria andIsDelIsNotNull() {
            addCriterion("isDel is not null");
            return (Criteria) this;
        }

        public Criteria andIsDelEqualTo(Boolean value) {
            addCriterion("isDel =", value, "isDel");
            return (Criteria) this;
        }

        public Criteria andIsDelNotEqualTo(Boolean value) {
            addCriterion("isDel <>", value, "isDel");
            return (Criteria) this;
        }

        public Criteria andIsDelGreaterThan(Boolean value) {
            addCriterion("isDel >", value, "isDel");
            return (Criteria) this;
        }

        public Criteria andIsDelGreaterThanOrEqualTo(Boolean value) {
            addCriterion("isDel >=", value, "isDel");
            return (Criteria) this;
        }

        public Criteria andIsDelLessThan(Boolean value) {
            addCriterion("isDel <", value, "isDel");
            return (Criteria) this;
        }

        public Criteria andIsDelLessThanOrEqualTo(Boolean value) {
            addCriterion("isDel <=", value, "isDel");
            return (Criteria) this;
        }

        public Criteria andIsDelIn(List<Boolean> values) {
            addCriterion("isDel in", values, "isDel");
            return (Criteria) this;
        }

        public Criteria andIsDelNotIn(List<Boolean> values) {
            addCriterion("isDel not in", values, "isDel");
            return (Criteria) this;
        }

        public Criteria andIsDelBetween(Boolean value1, Boolean value2) {
            addCriterion("isDel between", value1, value2, "isDel");
            return (Criteria) this;
        }

        public Criteria andIsDelNotBetween(Boolean value1, Boolean value2) {
            addCriterion("isDel not between", value1, value2, "isDel");
            return (Criteria) this;
        }

        public Criteria andCustomer1IsNull() {
            addCriterion("customer1 is null");
            return (Criteria) this;
        }

        public Criteria andCustomer1IsNotNull() {
            addCriterion("customer1 is not null");
            return (Criteria) this;
        }

        public Criteria andCustomer1EqualTo(String value) {
            addCriterion("customer1 =", value, "customer1");
            return (Criteria) this;
        }

        public Criteria andCustomer1NotEqualTo(String value) {
            addCriterion("customer1 <>", value, "customer1");
            return (Criteria) this;
        }

        public Criteria andCustomer1GreaterThan(String value) {
            addCriterion("customer1 >", value, "customer1");
            return (Criteria) this;
        }

        public Criteria andCustomer1GreaterThanOrEqualTo(String value) {
            addCriterion("customer1 >=", value, "customer1");
            return (Criteria) this;
        }

        public Criteria andCustomer1LessThan(String value) {
            addCriterion("customer1 <", value, "customer1");
            return (Criteria) this;
        }

        public Criteria andCustomer1LessThanOrEqualTo(String value) {
            addCriterion("customer1 <=", value, "customer1");
            return (Criteria) this;
        }

        public Criteria andCustomer1Like(String value) {
            addCriterion("customer1 like", value, "customer1");
            return (Criteria) this;
        }

        public Criteria andCustomer1NotLike(String value) {
            addCriterion("customer1 not like", value, "customer1");
            return (Criteria) this;
        }

        public Criteria andCustomer1In(List<String> values) {
            addCriterion("customer1 in", values, "customer1");
            return (Criteria) this;
        }

        public Criteria andCustomer1NotIn(List<String> values) {
            addCriterion("customer1 not in", values, "customer1");
            return (Criteria) this;
        }

        public Criteria andCustomer1Between(String value1, String value2) {
            addCriterion("customer1 between", value1, value2, "customer1");
            return (Criteria) this;
        }

        public Criteria andCustomer1NotBetween(String value1, String value2) {
            addCriterion("customer1 not between", value1, value2, "customer1");
            return (Criteria) this;
        }

        public Criteria andCustomer2IsNull() {
            addCriterion("customer2 is null");
            return (Criteria) this;
        }

        public Criteria andCustomer2IsNotNull() {
            addCriterion("customer2 is not null");
            return (Criteria) this;
        }

        public Criteria andCustomer2EqualTo(String value) {
            addCriterion("customer2 =", value, "customer2");
            return (Criteria) this;
        }

        public Criteria andCustomer2NotEqualTo(String value) {
            addCriterion("customer2 <>", value, "customer2");
            return (Criteria) this;
        }

        public Criteria andCustomer2GreaterThan(String value) {
            addCriterion("customer2 >", value, "customer2");
            return (Criteria) this;
        }

        public Criteria andCustomer2GreaterThanOrEqualTo(String value) {
            addCriterion("customer2 >=", value, "customer2");
            return (Criteria) this;
        }

        public Criteria andCustomer2LessThan(String value) {
            addCriterion("customer2 <", value, "customer2");
            return (Criteria) this;
        }

        public Criteria andCustomer2LessThanOrEqualTo(String value) {
            addCriterion("customer2 <=", value, "customer2");
            return (Criteria) this;
        }

        public Criteria andCustomer2Like(String value) {
            addCriterion("customer2 like", value, "customer2");
            return (Criteria) this;
        }

        public Criteria andCustomer2NotLike(String value) {
            addCriterion("customer2 not like", value, "customer2");
            return (Criteria) this;
        }

        public Criteria andCustomer2In(List<String> values) {
            addCriterion("customer2 in", values, "customer2");
            return (Criteria) this;
        }

        public Criteria andCustomer2NotIn(List<String> values) {
            addCriterion("customer2 not in", values, "customer2");
            return (Criteria) this;
        }

        public Criteria andCustomer2Between(String value1, String value2) {
            addCriterion("customer2 between", value1, value2, "customer2");
            return (Criteria) this;
        }

        public Criteria andCustomer2NotBetween(String value1, String value2) {
            addCriterion("customer2 not between", value1, value2, "customer2");
            return (Criteria) this;
        }

        public Criteria andCustomer3IsNull() {
            addCriterion("customer3 is null");
            return (Criteria) this;
        }

        public Criteria andCustomer3IsNotNull() {
            addCriterion("customer3 is not null");
            return (Criteria) this;
        }

        public Criteria andCustomer3EqualTo(String value) {
            addCriterion("customer3 =", value, "customer3");
            return (Criteria) this;
        }

        public Criteria andCustomer3NotEqualTo(String value) {
            addCriterion("customer3 <>", value, "customer3");
            return (Criteria) this;
        }

        public Criteria andCustomer3GreaterThan(String value) {
            addCriterion("customer3 >", value, "customer3");
            return (Criteria) this;
        }

        public Criteria andCustomer3GreaterThanOrEqualTo(String value) {
            addCriterion("customer3 >=", value, "customer3");
            return (Criteria) this;
        }

        public Criteria andCustomer3LessThan(String value) {
            addCriterion("customer3 <", value, "customer3");
            return (Criteria) this;
        }

        public Criteria andCustomer3LessThanOrEqualTo(String value) {
            addCriterion("customer3 <=", value, "customer3");
            return (Criteria) this;
        }

        public Criteria andCustomer3Like(String value) {
            addCriterion("customer3 like", value, "customer3");
            return (Criteria) this;
        }

        public Criteria andCustomer3NotLike(String value) {
            addCriterion("customer3 not like", value, "customer3");
            return (Criteria) this;
        }

        public Criteria andCustomer3In(List<String> values) {
            addCriterion("customer3 in", values, "customer3");
            return (Criteria) this;
        }

        public Criteria andCustomer3NotIn(List<String> values) {
            addCriterion("customer3 not in", values, "customer3");
            return (Criteria) this;
        }

        public Criteria andCustomer3Between(String value1, String value2) {
            addCriterion("customer3 between", value1, value2, "customer3");
            return (Criteria) this;
        }

        public Criteria andCustomer3NotBetween(String value1, String value2) {
            addCriterion("customer3 not between", value1, value2, "customer3");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}