// ============================================================
// CONFIGURATION
// ============================================================

const API_BASE_URL = "http://localhost:8080";


// ============================================================
// GLOBAL DATA
// ============================================================

let allExpenses = [];

let filteredExpenses = [];

let statisticsExpenses = [];

let expenseIdToDelete = null;


// ============================================================
// DOM REFERENCES
// ============================================================

const expenseForm =
    document.getElementById("expenseForm");

const expenseList =
    document.getElementById("expenseList");

const emptyExpenses =
    document.getElementById("emptyExpenses");

const editSection =
    document.getElementById("editSection");

const editExpenseForm =
    document.getElementById("editExpenseForm");

const deleteSection =
    document.getElementById("deleteSection");

const confirmDeleteButton =
    document.getElementById("confirmDeleteButton");

const cancelDeleteButton =
    document.getElementById("cancelDeleteButton");


// ============================================================
// PAGE NAVIGATION
// ============================================================

const navButtons =
    document.querySelectorAll(".nav-button");

const pages =
    document.querySelectorAll(".page");


navButtons.forEach(button => {

    button.addEventListener(
        "click",
        function() {

            const targetPage =
                button.dataset.page;


            // Hide all pages

            pages.forEach(page => {

                page.classList.remove(
                    "active-page"
                );

            });


            // Remove active state

            navButtons.forEach(navButton => {

                navButton.classList.remove(
                    "active"
                );

            });


            // Show selected page

            document
                .getElementById(targetPage)
                .classList.add(
                    "active-page"
                );


            // Activate selected button

            button.classList.add("active");


            // Recalculate statistics
            // when statistics page is opened

            if (targetPage === "statisticsPage") {

                applyStatisticsFilters();

            }

        }
    );

});


// ============================================================
// NOTIFICATIONS
// ============================================================

function showNotification(
    message,
    type = "success"
) {

    const container =
        document.getElementById(
            "notificationContainer"
        );


    const notification =
        document.createElement("div");


    notification.className =
        `notification ${type}`;


    const title =
        type === "error"
            ? "Error"
            : "Success";


    notification.innerHTML = `

        <div class="notification-title">
            ${title}
        </div>

        <div class="notification-message">
        </div>

    `;


    notification
        .querySelector(
            ".notification-message"
        )
        .textContent = message;


    container.appendChild(
        notification
    );


    setTimeout(() => {

        notification.remove();

    }, 3500);

}


// ============================================================
// BACKEND RESPONSE HANDLING
// ============================================================

async function parseResponse(response) {

    const contentType =
        response.headers.get(
            "content-type"
        ) || "";


    if (
        contentType.includes(
            "application/json"
        )
    ) {

        return await response.json();

    }


    return await response.text();

}


async function handleResponse(response) {

    const data =
        await parseResponse(response);


    if (!response.ok) {

        let message =
            "Request failed.";


        if (
            data &&
            typeof data === "object"
        ) {

            const messages =
                Object.values(data)
                    .filter(Boolean);


            if (messages.length > 0) {

                message =
                    messages.join(" | ");

            }

        }

        else if (data) {

            message = data;

        }


        throw new Error(message);

    }


    return data;

}


// ============================================================
// ADD EXPENSE
// ============================================================

expenseForm.addEventListener(
    "submit",
    async function(event) {

        event.preventDefault();


        const data = {

            amount:
                document
                    .getElementById("amount")
                    .value,

            category:
                document
                    .getElementById("category")
                    .value,

            description:
                document
                    .getElementById("description")
                    .value,

            date:
                document
                    .getElementById("date")
                    .value,

            paymentMethod:
                document
                    .getElementById("paymentMethod")
                    .value

        };


        try {

            const response =
                await fetch(
                    `${API_BASE_URL}/expenses`,
                    {

                        method: "POST",

                        headers: {

                            "Content-Type":
                                "application/json"

                        },

                        body:
                            JSON.stringify(data)

                    }
                );


            await handleResponse(
                response
            );


            expenseForm.reset();


            showNotification(
                "Expense added successfully!"
            );


            await loadExpenses();


            // Automatically move
            // user to Expenses page

            switchToPage(
                "expensesPage"
            );

        }

        catch (error) {

            console.error(error);


            showNotification(
                error.message,
                "error"
            );

        }

    }
);


// ============================================================
// GET ALL EXPENSES
// ============================================================

async function loadExpenses() {

    try {

        const response =
            await fetch(
                `${API_BASE_URL}/expenses`
            );


        const expenses =
            await handleResponse(
                response
            );


        allExpenses =
            Array.isArray(expenses)
                ? expenses
                : [];


        filteredExpenses =
            [...allExpenses];


        statisticsExpenses =
            [...allExpenses];


        displayExpenses(
            filteredExpenses
        );


        updateStatistics(
            statisticsExpenses
        );

    }

    catch (error) {

        console.error(error);


        showNotification(
            "Could not load expenses: " +
            error.message,
            "error"
        );

    }

}


// ============================================================
// DISPLAY EXPENSES
// ============================================================

function displayExpenses(
    expenses
) {

    expenseList.innerHTML = "";


    if (expenses.length === 0) {

        emptyExpenses.style.display =
            "block";

        return;

    }


    emptyExpenses.style.display =
        "none";


    expenses.forEach(expense => {

        const expenseItem =
            document.createElement("div");


        expenseItem.className =
            "expense-item";


        expenseItem.innerHTML = `

            <div class="expense-top">

                <p class="expense-amount">
                </p>

                <span class="expense-category">
                </span>

            </div>


            <div class="expense-details">

                <div class="expense-detail">

                    <span>
                        Description
                    </span>

                    <strong
                        class="expense-description">
                    </strong>

                </div>


                <div class="expense-detail">

                    <span>
                        Date
                    </span>

                    <strong
                        class="expense-date">
                    </strong>

                </div>


                <div class="expense-detail">

                    <span>
                        Payment
                    </span>

                    <strong
                        class="expense-payment">
                    </strong>

                </div>

            </div>


            <div class="expense-actions">

                <button
                    class="update-button"
                    type="button">

                    Update

                </button>


                <button
                    class="delete-button"
                    type="button">

                    Delete

                </button>

            </div>

        `;


        expenseItem
            .querySelector(
                ".expense-amount"
            )
            .textContent =
                "₹" +
                formatAmount(
                    expense.amount
                );


        expenseItem
            .querySelector(
                ".expense-category"
            )
            .textContent =
                formatLabel(
                    expense.category
                );


        expenseItem
            .querySelector(
                ".expense-description"
            )
            .textContent =
                expense.description ||
                "No description";


        expenseItem
            .querySelector(
                ".expense-date"
            )
            .textContent =
                expense.date;


        expenseItem
            .querySelector(
                ".expense-payment"
            )
            .textContent =
                formatLabel(
                    expense.paymentMethod
                );


        expenseItem
            .querySelector(
                ".update-button"
            )
            .addEventListener(
                "click",
                () =>
                    openEditModal(
                        expense.id
                    )
            );


        expenseItem
            .querySelector(
                ".delete-button"
            )
            .addEventListener(
                "click",
                () =>
                    openDeleteModal(
                        expense.id
                    )
            );


        expenseList.appendChild(
            expenseItem
        );

    });

}


// ============================================================
// UPDATE EXPENSE - OPEN MODAL
// ============================================================

async function openEditModal(id) {

    try {

        const response =
            await fetch(
                `${API_BASE_URL}/expenses/${id}`
            );


        const expense =
            await handleResponse(
                response
            );


        document.getElementById(
            "editId"
        ).value =
            expense.id;


        document.getElementById(
            "editAmount"
        ).value =
            expense.amount;


        document.getElementById(
            "editCategory"
        ).value =
            expense.category;


        document.getElementById(
            "editDescription"
        ).value =
            expense.description || "";


        document.getElementById(
            "editDate"
        ).value =
            expense.date;


        document.getElementById(
            "editPaymentMethod"
        ).value =
            expense.paymentMethod;


        editSection.style.display =
            "flex";

    }

    catch (error) {

        console.error(error);


        showNotification(
            "Could not load expense: " +
            error.message,
            "error"
        );

    }

}


// ============================================================
// UPDATE EXPENSE - SAVE
// ============================================================

editExpenseForm.addEventListener(
    "submit",
    async function(event) {

        event.preventDefault();


        const id =
            document.getElementById(
                "editId"
            ).value;


        const data = {

            amount:
                document.getElementById(
                    "editAmount"
                ).value,

            category:
                document.getElementById(
                    "editCategory"
                ).value,

            description:
                document.getElementById(
                    "editDescription"
                ).value,

            date:
                document.getElementById(
                    "editDate"
                ).value,

            paymentMethod:
                document.getElementById(
                    "editPaymentMethod"
                ).value

        };


        try {

            const response =
                await fetch(
                    `${API_BASE_URL}/expenses/${id}`,
                    {

                        method: "PUT",

                        headers: {

                            "Content-Type":
                                "application/json"

                        },

                        body:
                            JSON.stringify(data)

                    }
                );


            await handleResponse(
                response
            );


            closeEditModal();


            showNotification(
                "Expense updated successfully!"
            );


            await loadExpenses();

        }

        catch (error) {

            console.error(error);


            showNotification(
                error.message,
                "error"
            );

        }

    }
);


// ============================================================
// CLOSE UPDATE MODAL
// ============================================================

document
    .getElementById(
        "cancelEditButton"
    )
    .addEventListener(
        "click",
        closeEditModal
    );


document
    .getElementById(
        "closeEditButton"
    )
    .addEventListener(
        "click",
        closeEditModal
    );


function closeEditModal() {

    editSection.style.display =
        "none";


    editExpenseForm.reset();

}


// ============================================================
// DELETE EXPENSE - OPEN
// ============================================================

function openDeleteModal(id) {

    expenseIdToDelete = id;


    deleteSection.style.display =
        "flex";

}


// ============================================================
// DELETE EXPENSE - CLOSE
// ============================================================

function closeDeleteModal() {

    expenseIdToDelete = null;


    deleteSection.style.display =
        "none";

}


cancelDeleteButton.addEventListener(
    "click",
    closeDeleteModal
);


// ============================================================
// DELETE EXPENSE - CONFIRM
// ============================================================

confirmDeleteButton.addEventListener(
    "click",
    async function() {

        if (
            expenseIdToDelete === null
        ) {

            return;

        }


        const id =
            expenseIdToDelete;


        try {

            const response =
                await fetch(
                    `${API_BASE_URL}/expenses/${id}`,
                    {

                        method: "DELETE"

                    }
                );


            await handleResponse(
                response
            );


            closeDeleteModal();


            showNotification(
                "Expense deleted successfully!"
            );


            await loadExpenses();

        }

        catch (error) {

            console.error(error);


            closeDeleteModal();


            showNotification(
                error.message,
                "error"
            );

        }

    }
);


// ============================================================
// EXPENSE PAGE FILTERS
// ============================================================

document
    .getElementById(
        "filterButton"
    )
    .addEventListener(
        "click",
        applyFilters
    );


function applyFilters() {

    const category =
        document.getElementById(
            "filterCategory"
        ).value;


    const paymentMethod =
        document.getElementById(
            "filterPaymentMethod"
        ).value;


    const maxAmount =
        document.getElementById(
            "filterMaxAmount"
        ).value;


    const date =
        document.getElementById(
            "filterDate"
        ).value;


    filteredExpenses =
        allExpenses.filter(
            expense => {

                if (
                    category &&
                    expense.category !== category
                ) {

                    return false;

                }


                if (
                    paymentMethod &&
                    expense.paymentMethod !==
                    paymentMethod
                ) {

                    return false;

                }


                if (
                    maxAmount &&
                    Number(expense.amount) >
                    Number(maxAmount)
                ) {

                    return false;

                }


                if (
                    date &&
                    expense.date !== date
                ) {

                    return false;

                }


                return true;

            }
        );


    displayExpenses(
        filteredExpenses
    );


    showNotification(
        filteredExpenses.length +
        " matching expense(s) found."
    );

}


// ============================================================
// CLEAR EXPENSE FILTERS
// ============================================================

document
    .getElementById(
        "clearFilterButton"
    )
    .addEventListener(
        "click",
        function() {

            document.getElementById(
                "filterCategory"
            ).value = "";


            document.getElementById(
                "filterPaymentMethod"
            ).value = "";


            document.getElementById(
                "filterMaxAmount"
            ).value = "";


            document.getElementById(
                "filterDate"
            ).value = "";


            filteredExpenses =
                [...allExpenses];


            displayExpenses(
                filteredExpenses
            );


            showNotification(
                "Filters cleared."
            );

        }
    );


// ============================================================
// STATISTICS FILTERS
// ============================================================

document
    .getElementById(
        "statisticsFilterButton"
    )
    .addEventListener(
        "click",
        applyStatisticsFilters
    );


function applyStatisticsFilters() {

    const category =
        document.getElementById(
            "statisticsCategory"
        ).value;


    const paymentMethod =
        document.getElementById(
            "statisticsPaymentMethod"
        ).value;


    const maxAmount =
        document.getElementById(
            "statisticsMaxAmount"
        ).value;


    const date =
        document.getElementById(
            "statisticsDate"
        ).value;


    statisticsExpenses =
        allExpenses.filter(
            expense => {

                if (
                    category &&
                    expense.category !== category
                ) {

                    return false;

                }


                if (
                    paymentMethod &&
                    expense.paymentMethod !==
                    paymentMethod
                ) {

                    return false;

                }


                if (
                    maxAmount &&
                    Number(expense.amount) >
                    Number(maxAmount)
                ) {

                    return false;

                }


                if (
                    date &&
                    expense.date !== date
                ) {

                    return false;

                }


                return true;

            }
        );


    updateStatistics(
        statisticsExpenses
    );

}


// ============================================================
// CLEAR STATISTICS FILTERS
// ============================================================

document
    .getElementById(
        "statisticsClearButton"
    )
    .addEventListener(
        "click",
        function() {

            document.getElementById(
                "statisticsCategory"
            ).value = "";


            document.getElementById(
                "statisticsPaymentMethod"
            ).value = "";


            document.getElementById(
                "statisticsMaxAmount"
            ).value = "";


            document.getElementById(
                "statisticsDate"
            ).value = "";


            statisticsExpenses =
                [...allExpenses];


            updateStatistics(
                statisticsExpenses
            );


            showNotification(
                "Statistics filters cleared."
            );

        }
    );


// ============================================================
// STATISTICS
// ============================================================

function updateStatistics(expenses) {

    const total =
        expenses.reduce(
            (sum, expense) =>
                sum +
                Number(
                    expense.amount || 0
                ),
            0
        );


    document.getElementById(
        "totalAmount"
    ).textContent =
        "₹" +
        formatAmount(total);


    document.getElementById(
        "transactionCount"
    ).textContent =
        expenses.length;


    updateCategoryStats(
        expenses
    );


    updatePaymentMethodStats(
        expenses
    );


    updateDateRangeTotal(
        expenses
    );

}


// ============================================================
// CATEGORY STATISTICS
// ============================================================

function updateCategoryStats(
    expenses
) {

    const stats = {};


    expenses.forEach(expense => {

        const category =
            expense.category;


        if (!stats[category]) {

            stats[category] = 0;

        }


        stats[category] +=
            Number(
                expense.amount || 0
            );

    });


    const container =
        document.getElementById(
            "categoryStats"
        );


    container.innerHTML = "";


    const entries =
        Object.entries(stats);


    if (entries.length === 0) {

        container.innerHTML =
            "<p>No data available.</p>";

        return;

    }


    entries
        .sort(
            (a, b) =>
                b[1] - a[1]
        )
        .forEach(
            ([category, amount]) => {

                const row =
                    document.createElement(
                        "div"
                    );


                row.className =
                    "stat-row";


                const name =
                    document.createElement(
                        "span"
                    );


                name.textContent =
                    formatLabel(
                        category
                    );


                const value =
                    document.createElement(
                        "strong"
                    );


                value.textContent =
                    "₹" +
                    formatAmount(
                        amount
                    );


                row.appendChild(name);

                row.appendChild(value);


                container.appendChild(row);

            }
        );

}


// ============================================================
// PAYMENT METHOD STATISTICS
// ============================================================

function updatePaymentMethodStats(
    expenses
) {

    const stats = {};


    expenses.forEach(expense => {

        const paymentMethod =
            expense.paymentMethod;


        if (!stats[paymentMethod]) {

            stats[paymentMethod] = 0;

        }


        stats[paymentMethod] +=
            Number(
                expense.amount || 0
            );

    });


    const container =
        document.getElementById(
            "paymentMethodStats"
        );


    container.innerHTML = "";


    const entries =
        Object.entries(stats);


    if (entries.length === 0) {

        container.innerHTML =
            "<p>No data available.</p>";

        return;

    }


    entries
        .sort(
            (a, b) =>
                b[1] - a[1]
        )
        .forEach(
            ([paymentMethod, amount]) => {

                const row =
                    document.createElement(
                        "div"
                    );


                row.className =
                    "stat-row";


                const name =
                    document.createElement(
                        "span"
                    );


                name.textContent =
                    formatLabel(
                        paymentMethod
                    );


                const value =
                    document.createElement(
                        "strong"
                    );


                value.textContent =
                    "₹" +
                    formatAmount(
                        amount
                    );


                row.appendChild(name);

                row.appendChild(value);


                container.appendChild(row);

            }
        );

}


// ============================================================
// DATE RANGE STATISTICS
// ============================================================

document
    .getElementById(
        "dateStatsButton"
    )
    .addEventListener(
        "click",
        function() {

            const startDate =
                document.getElementById(
                    "startDate"
                ).value;


            const endDate =
                document.getElementById(
                    "endDate"
                ).value;


            if (
                !startDate ||
                !endDate
            ) {

                showNotification(
                    "Please select both start and end dates.",
                    "error"
                );

                return;

            }


            if (
                startDate > endDate
            ) {

                showNotification(
                    "Start date cannot be after end date.",
                    "error"
                );

                return;

            }


            const expensesInRange =
                statisticsExpenses.filter(
                    expense =>
                        expense.date >= startDate &&
                        expense.date <= endDate
                );


            const total =
                expensesInRange.reduce(
                    (sum, expense) =>
                        sum +
                        Number(
                            expense.amount || 0
                        ),
                    0
                );


            document.getElementById(
                "dateRangeTotal"
            ).textContent =
                "₹" +
                formatAmount(total);


            showNotification(
                "Date range total calculated."
            );

        }
    );


// ============================================================
// UPDATE DATE RANGE TOTAL
// ============================================================

function updateDateRangeTotal(
    expenses
) {

    const startDate =
        document.getElementById(
            "startDate"
        ).value;


    const endDate =
        document.getElementById(
            "endDate"
        ).value;


    if (
        !startDate ||
        !endDate
    ) {

        document.getElementById(
            "dateRangeTotal"
        ).textContent =
            "₹0.00";

        return;

    }


    if (
        startDate > endDate
    ) {

        document.getElementById(
            "dateRangeTotal"
        ).textContent =
            "₹0.00";

        return;

    }


    const total =
        expenses
            .filter(
                expense =>
                    expense.date >= startDate &&
                    expense.date <= endDate
            )
            .reduce(
                (sum, expense) =>
                    sum +
                    Number(
                        expense.amount || 0
                    ),
                0
            );


    document.getElementById(
        "dateRangeTotal"
    ).textContent =
        "₹" +
        formatAmount(total);

}


// ============================================================
// PAGE SWITCH HELPER
// ============================================================

function switchToPage(
    pageId
) {

    pages.forEach(page => {

        page.classList.remove(
            "active-page"
        );

    });


    navButtons.forEach(button => {

        button.classList.remove(
            "active"
        );

    });


    const page =
        document.getElementById(
            pageId
        );


    const button =
        document.querySelector(
            `.nav-button[data-page="${pageId}"]`
        );


    if (page) {

        page.classList.add(
            "active-page"
        );

    }


    if (button) {

        button.classList.add(
            "active"
        );

    }

}


// ============================================================
// UTILITY FUNCTIONS
// ============================================================

function formatAmount(amount) {

    return Number(
        amount || 0
    ).toFixed(2);

}


function formatLabel(value) {

    if (!value) {

        return "";

    }


    return value
        .toLowerCase()
        .split("_")
        .map(
            word =>
                word.charAt(0).toUpperCase() +
                word.slice(1)
        )
        .join(" ");

}


// ============================================================
// INITIAL LOAD
// ============================================================

loadExpenses();