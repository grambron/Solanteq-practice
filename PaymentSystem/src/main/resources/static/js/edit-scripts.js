var deleteBankUrl = "";
var deleteBankBin = "";

function fill_inputs() {
    let dataSource = document.getElementById("banks");
    let data = dataSource.options[dataSource.selectedIndex].text.split(' ');
    let binBank = document.getElementById("bin");
    binBank.value = data[0];
    deleteBankBin = data[0];
    let urlBank = document.getElementById("url");
    urlBank.value = data[2];
    deleteBankUrl = data[2];
}

function sendForm() {
    let xhr = getRequest("/edit");
    let stateField = document.getElementById("status");
    xhr.onload = function () {
        if (xhr.status === 200) {
            stateField.innerHTML = "Successfully changed (update page)";
        }
        else {
            stateField.innerHTML = xhr.responseText;
        }

    };
    let newBin = document.getElementById("bin").value;
    let newUrl = document.getElementById("url").value;
    if (validate(newBin, newUrl)) {
        stateField.innerHTML = "Incorrect bin";
        return;
    }
    if (deleteBankBin === "" || deleteBankUrl === "") {
        stateField.innerHTML = "Choose bank to edit";
        return;
    }
    let data = JSON.stringify({
        "delBin": deleteBankBin,
        "delUrl": deleteBankUrl,
        "newBin": newBin,
        "newUrl": newUrl
    });
    xhr.send(data);
}
