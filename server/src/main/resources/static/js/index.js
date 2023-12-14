
function showQueries(index) {
    if (showingQueriesForIndex == index) {
        showingQueriesForIndex = -1;
    }
    else {
        showingQueriesForIndex = index
    }
    updateDisplay()
}

function updateDisplay() {
    var targetDiv = document.getElementById('row'+showingQueriesForIndex);
    removeAllRowsWithQueries()
    if (targetDiv) {
        var newRow = document.createElement('tr');
        var newCell = document.createElement('td');
        newCell.colSpan = 4;
        //newCell.textContent = transactions[showingQueriesForIndex].originalQueries;
        var newList = document.createElement('ul');

        transactions[showingQueriesForIndex].originalQueries.forEach(function(item) {
            var listItem = document.createElement('li');
            listItem.classList.add('list-item');
            listItem.textContent = item;
            newList.appendChild(listItem);
        });
        newCell.appendChild(newList);
        newRow.appendChild(newCell);

        newRow.classList.add('row-original-queries');
        newCell.classList.add('list-original-queries');
        // Insert the new div after the target div
        targetDiv.parentNode.insertBefore(newRow, targetDiv.nextSibling);
    }
}

function removeAllRowsWithQueries() {
    var elementsToRemove = document.getElementsByClassName('row-original-queries');

    // Convert the HTMLCollection to an array for easier iteration
    var elementsArray = Array.from(elementsToRemove);

    // Iterate through the array and remove each element
    elementsArray.forEach(function(element) {
        element.parentNode.removeChild(element);
    });
}