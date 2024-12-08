package com.abhirambsn.expenseservice.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.query.Criteria;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Stack;

@Slf4j
public class QueryParser {
    public static Criteria parseFilter(String filter) {
        // Remove surrounding parentheses if present
        filter = URLDecoder.decode(filter, StandardCharsets.UTF_8);
        filter = filter.trim();
        if (filter.startsWith("(") && filter.endsWith(")")) {
            filter = filter.substring(1, filter.length() - 1);
        }

        return parseExpression(filter);
    }

    private static Criteria parseExpression(String expression) {
        Stack<Criteria> criteriaStack = new Stack<>();
        Stack<String> operatorStack = new Stack<>();

        String[] tokens = tokenize(expression);
        log.info("Tokens: {}", (Object) tokens);

        for (String token : tokens) {
            log.info("Parsing token: {}", token);
            if (token.trim().equalsIgnoreCase("and") || token.trim().equalsIgnoreCase("or")) {
                operatorStack.push(token.trim().toLowerCase());
            } else if (token.equals("(")) {
                // Push a marker for a new subexpression
                operatorStack.push("(");
            } else if (token.equals(")")) {
                while (!operatorStack.isEmpty() && !operatorStack.peek().equals("(")) {
                    resolveTopCriteria(criteriaStack, operatorStack.pop());
                }
                operatorStack.pop();
            } else {
                criteriaStack.push(parseCondition(token));
            }
        }

        while (!operatorStack.isEmpty()) {
            resolveTopCriteria(criteriaStack, operatorStack.pop());
        }

        return criteriaStack.pop();
    }

    private static void resolveTopCriteria(Stack<Criteria> criteriaStack, String operator) {
        Criteria right = criteriaStack.pop();
        Criteria left = criteriaStack.pop();

        log.warn("Resolving criteria: {} {} {}", left.getCriteriaObject().toJson(), operator, right.getCriteriaObject().toJson());
        log.warn("Length of stack: {}", criteriaStack.size());

        if (operator.equals("and")) {
            criteriaStack.push(new Criteria().andOperator(left, right));
        } else if (operator.equals("or")) {
            criteriaStack.push(new Criteria().orOperator(left, right));
        }
        log.info("Size of criteria stack: {}", criteriaStack.size());
        log.info("Criteria stack: {}", criteriaStack.peek().getCriteriaObject().toJson());
    }

    private static Criteria parseCondition(String condition) {
        String[] parts = condition.trim().split(" ", 3);
        if (parts.length != 3) {
            throw new IllegalArgumentException("Invalid condition: " + condition);
        }

        String field = parts[0];
        String operator = parts[1];
        String value = parts[2];

        log.info("field: {}, operator: {}, value: {}", field, operator, value);

        return switch (operator.toLowerCase()) {
            case "eq" -> Criteria.where(field).is(parseValue(value));
            case "lt" -> Criteria.where(field).lt(parseValue(value));
            case "gt" -> Criteria.where(field).gt(parseValue(value));
            case "le" -> Criteria.where(field).lte(parseValue(value));
            case "ge" -> Criteria.where(field).gte(parseValue(value));
            case "sw" -> Criteria.where(field).regex("^" + parseValue(value));
            default -> throw new IllegalArgumentException("Unsupported operator: " + operator);
        };
    }

    private static String[] tokenize(String expression) {
        // Tokenize the expression while respecting parentheses and spaces
        return expression.split("(?<!\\w)\\s+(?!\\w)|(?<=[()])|(?=[()])");
    }

    private static Object parseValue(String value) {
        // Try to parse numbers, else return the raw string
        try {
            return Double.valueOf(value);
        } catch (NumberFormatException e) {
            return value.replaceAll("^\"|\"$", ""); // Remove quotes from string values
        }
    }
}
