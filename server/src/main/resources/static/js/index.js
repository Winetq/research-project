function showQueries(index, key) {
    if (showingQueriesForIndex == index) {
        showingQueriesForIndex = -1;
    } else {
        showingQueriesForIndex = index;
    }
    update(key);
}

function update(key) {
    var targetDiv = document.getElementById('row'+showingQueriesForIndex);
    removeAllRowsWithQueries();
    if (targetDiv) {
        var newRow = document.createElement('tr');
        var newCell = document.createElement('td');
        newCell.colSpan = 4;
        var newList = document.createElement('ul');

        var transactions;
        for (const [k, value] of Object.entries(queriesToTransactions)) {
            if (k == key) {
                transactions = value;
                break;
            }
        }

        transactions.forEach(function(transaction) {
            var listItem = document.createElement('li');
            listItem.classList.add('list-item');
            listItem.textContent = transaction.originalQuery.split(';').join('\n');
            newList.appendChild(listItem);
        });

        newCell.appendChild(newList);
        newRow.appendChild(newCell);

        newRow.classList.add('row-original-queries');
        newCell.classList.add('list-original-queries');

        targetDiv.parentNode.insertBefore(newRow, targetDiv.nextSibling);
    }
}

function removeAllRowsWithQueries() {
    var elementsToRemove = document.getElementsByClassName('row-original-queries');
    var elementsArray = Array.from(elementsToRemove);
    elementsArray.forEach(function(element) {
        element.parentNode.removeChild(element);
    });
}