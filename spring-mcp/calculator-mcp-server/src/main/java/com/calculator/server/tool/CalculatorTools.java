package com.calculator.server.tool;

import org.springframework.ai.mcp.annotation.McpTool;
import org.springframework.ai.mcp.annotation.McpToolParam;
import org.springframework.stereotype.Component;

@Component 
public class CalculatorTools {

    @McpTool(name = "add", description = "Add two numbers")
    public double add(@McpToolParam(description = "First number", required = true) double a, 
                      @McpToolParam(description = "Second number", required = true) double b) {

        System.out.println("=============CalculatorTools Add =====================");                

        return a + b;
    }


    @McpTool(name = "subtract", description = "Subtract second number from first number")
    public double subtract(@McpToolParam(description = "First number", required = true) double a, 
                      @McpToolParam(description = "Second number", required = true) double b) {

        System.out.println("=============CalculatorTools substract =====================");
        return a - b;
    }


    @McpTool(name = "multiply", description = "multiply two numbers")
    public double multiply(@McpToolParam(description = "First number", required = true) double a, 
                      @McpToolParam(description = "Second number", required = true) double b) {

        System.out.println("=============CalculatorTools multiply =====================");
        return a * b;
    }


    @McpTool(name = "divide", description = "Divide first number by second number")
    public double divide(@McpToolParam(description = "First number", required = true) double a, 
                      @McpToolParam(description = "Second number", required = true) double b) {
        if (b == 0) {
            throw new IllegalArgumentException("Cannot divide by zero");
        }

        System.out.println("=============CalculatorTools division =====================");
        return a + b;
    }

    
}