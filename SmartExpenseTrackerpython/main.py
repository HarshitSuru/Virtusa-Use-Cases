import json
import os
from datetime import datetime
import matplotlib.pyplot as plt
from collections import defaultdict
FILE_NAME = "expenses.json"

def load_data():
    if not os.path.exists(FILE_NAME):
        return []
    with open(FILE_NAME, "r") as file:
        return json.load(file)
    
def save_data(data):
    with open(FILE_NAME, "w") as file:
        json.dump(data, file, indent=4)
        
def add_expense():
    date_input = input("Enter date (DD-MM-YYYY): ")    
    try:
        date_obj = datetime.strptime(date_input, "%d-%m-%Y")
        date = date_obj.strftime("%Y-%m-%d")
    except ValueError:
        print("Invalid date format! Please enter in DD-MM-YYYY.\n")
        return
    category = input("Enter category (Food, Travel, Bills...): ")
    try:
        amount = float(input("Enter amount: "))
    except ValueError:
        print("Invalid amount!\n")
        return
    description = input("Enter description: ")
    expense = {
        "date": date,
        "category": category,
        "amount": amount,
        "description": description
    }
    data = load_data()
    data.append(expense)
    save_data(data)
    print("Expense added successfully!\n")

def monthly_summary(month, year):
    data = load_data()
    total = 0
    count = 0
    for exp in data:
        d = datetime.strptime(exp["date"], "%d-%m-%Y")
        if d.month == month and d.year == year:
            total += exp["amount"]
            count += 1
    print(f"\n Summary for {month}/{year}")
    print(f"Total Expenses: ₹{total}")
    print(f"Transactions: {count}\n")

def category_analysis(month, year):
    data = load_data()
    category_data = defaultdict(float)
    for exp in data:
        d = datetime.strptime(exp["date"], "%d-%m-%Y")
        if d.month == month and d.year == year:
            category_data[exp["category"]] += exp["amount"]
    if not category_data:
        print("No data for this month.")
        return
    highest = max(category_data, key=category_data.get)
    print(f"Highest Spending Category: {highest} (₹{category_data[highest]})\n")
    categories = list(category_data.keys())
    amounts = list(category_data.values())
    plt.figure()
    plt.pie(amounts, labels=categories, autopct='%1.1f%%')
    plt.title(f"Category Breakdown ({month}/{year})")
    plt.show()

def menu():
    while True:
        print("====== Smart Expense Tracker ======")
        print("1. Add Expense")
        print("2. Monthly Summary")
        print("3. Category Analysis")
        print("4. Exit")

        choice = input("Choose option: ")

        if choice == "1":
            add_expense()
        elif choice == "2":
            m = int(input("Enter month: "))
            y = int(input("Enter year: "))
            monthly_summary(m, y)
        elif choice == "3":
            m = int(input("Enter month: "))
            y = int(input("Enter year: "))
            category_analysis(m, y)
        elif choice == "4":
            break
        else:
            print("Invalid choice.\n")

if __name__ == "__main__":
    menu()