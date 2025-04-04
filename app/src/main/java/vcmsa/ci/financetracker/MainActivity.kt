package vcmsa.ci.financetracker

import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var incomeInput: EditText
    private lateinit var expenseNameInputs: List<EditText>
    private lateinit var expenseAmountInputs: List<EditText>
    private lateinit var calculateButton: Button
    private lateinit var resetButton: Button

    private lateinit var incomeResult: TextView
    private lateinit var expensesResult: TextView
    private lateinit var balanceResult: TextView
    private lateinit var feedbackResult: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        incomeInput = findViewById(R.id.incomeInput)

        expenseNameInputs = listOf(
            findViewById(R.id.tvexpenseName1),
            findViewById(R.id.tvexpenseName2),
            findViewById(R.id.tvexpenseName3),
            findViewById(R.id.tvexpenseName4)
        )

        expenseAmountInputs = listOf(
            findViewById(R.id.tvexpenseAmount1),
            findViewById(R.id.tvexpenseAmount2),
            findViewById(R.id.tvexpenseAmount3),
            findViewById(R.id.tvexpenseAmount4)
        )

        incomeResult = findViewById(R.id.tvincomeResult)
        expensesResult = findViewById(R.id.tvexpensesResult)
        balanceResult = findViewById(R.id.tvbalanceResult)
        feedbackResult = findViewById(R.id.tvfeedbackResult)

        // Button
        calculateButton = findViewById(R.id.btnCalculate)
        resetButton = findViewById(R.id.btnReset)

        // Button listener
        calculateButton.setOnClickListener {
            processData()
            resetButton.setOnClickListener{
                resetFields()
            }
        }
    }

    private fun resetFields() {
        incomeInput.text.clear()

        for (input in expenseNameInputs) {
            input.text.clear()
        }

        for (input in expenseAmountInputs) {
            input.text.clear()
        }

        incomeResult.text = ""
        expensesResult.text = ""
        balanceResult.text = ""
        feedbackResult.text = ""
    }

    private fun processData() {
        val incomeText = incomeInput.text.toString()

        if (incomeText.isBlank() || !isNumeric(incomeText)) {
            showToast("Please enter a valid income")
            return
        }

        val income = incomeText.toDouble()
        var totalExpenses = 0.0
        val expenses = mutableListOf<Pair<String, Double>>()

        for (i in 0..3) {
            val name = expenseNameInputs[i].text.toString()
            val amountText = expenseAmountInputs[i].text.toString()

            if (name.isBlank() || amountText.isBlank() || !isNumeric(amountText)) {
                showToast("Please fill in all expense fields correctly")
                return
            }

            val amount = amountText.toDouble()
            totalExpenses += amount
            expenses.add(Pair(name, amount))
        }

        // Calculate balance
        val balance = income - totalExpenses

        // Display income, expenses, and balance
        incomeResult.text = "Total Income: $%.2f".format(income)
        expensesResult.text = "Total Expenses: $%.2f".format(totalExpenses)
        balanceResult.text = "Balance: $%.2f".format(balance)

        // Set balance color and general feedback
        if (balance >= 0) {
            balanceResult.setTextColor(Color.GREEN)
            feedbackResult.setTextColor(Color.GREEN)
            feedbackResult.text = "You are saving money!"
        } else {
            balanceResult.setTextColor(Color.RED)
            feedbackResult.setTextColor(Color.RED)
            feedbackResult.text = "You are overspending!"
        }

        // Expense feedback per category
        val breakdown = StringBuilder("\n\nExpense Breakdown:\n")
        for ((name, amount) in expenses) {
            val percentage = (amount / income) * 100
            breakdown.append("- $name: %.2f%% of income. ".format(percentage))

            when {
                percentage > 30 -> breakdown.append("Too high!\n")
                percentage < 5 -> breakdown.append("Very low, good job!\n")
                else -> breakdown.append("Reasonable.\n")
            }
        }

        feedbackResult.append(breakdown)
    }

    private fun isNumeric(str: String): Boolean {
        return str.toDoubleOrNull() != null
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}