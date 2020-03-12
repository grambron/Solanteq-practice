function getRequest(tag) {
    let xhr = new XMLHttpRequest();
    xhr.open('POST', tag, true);
    xhr.setRequestHeader("Content-Type", "application/json");
    return xhr;
}


function validate(bin, url) {
    return bin.length !== 6 || isNaN(parseInt(bin)) || (url.empty === "") || (url.length > 30) || (parseInt(bin) <= 0);
}