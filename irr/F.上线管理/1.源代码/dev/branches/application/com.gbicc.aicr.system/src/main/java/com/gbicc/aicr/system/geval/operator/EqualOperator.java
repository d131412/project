package com.gbicc.aicr.system.geval.operator;

import com.gbicc.aicr.system.geval.EvaluationConstants;


/**
 * The equal operator.
 */
public class EqualOperator extends AbstractOperator {

	/**
	 * Default constructor.
	 */
	public EqualOperator() {
		super("==", 3);
	}

	/**
	 * Evaluates two double operands.
	 * 
	 * @param leftOperand
	 *            The left operand being evaluated.
	 * @param rightOperand
	 *            The right operand being evaluated.
	 */
	@Override
	public double evaluate(final double leftOperand, final double rightOperand) {
		if (leftOperand == rightOperand) {
			return 1;
		}

		return 0;
	}

	/**
	 * Evaluates two string operands.
	 * 
	 * @param leftOperand
	 *            The left operand being evaluated.
	 * @param rightOperand
	 *            The right operand being evaluated.
	 */
	@Override
	public String evaluate(String leftOperand, String rightOperand) {
		if (leftOperand.compareTo(rightOperand) == 0) {
			return EvaluationConstants.BOOLEAN_STRING_TRUE;
		}

		return EvaluationConstants.BOOLEAN_STRING_FALSE;
	}
}