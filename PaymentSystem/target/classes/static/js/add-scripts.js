function sendForm() {
    let xhr = getRequest("/add");
    let stateField = document.getElementById("status");
    xhr.onload = function () {
        if (xhr.status === 200) {
            stateField.innerHTML = "Successfully changed (update page)";
        } else {
            stateField.innerHTML = xhr.responseText;
        }
    };
    let newBin = document.getElementById("bin").value;
    let newUrl = document.getElementById("url").value;
    if (validate(newBin, newUrl)) {
        stateField.innerHTML = "Incorrect bin";
        return;
    }
    let data = JSON.stringify({
        "newBin": newBin,
        "newUrl": newUrl
    });
    xhr.send(data);
}