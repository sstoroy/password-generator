function showElement(elementID) {document.getElementById(elementID).style.display = 'block';}
function hideElement(elementID) {document.getElementById(elementID).style.display = 'none';}

function showAPI() {
    showElement("APIInfo");
    hideElement("showAPIInfoButton");
}

function getFormValues(formID) {
    const form = document.getElementById(formID);
    const formValues = new Map();
    if (!form) {
        console.error(`Form with ID "${formID}" not found.`);
        return formValues;
    }

    const formElements = form.elements;
    for (let element of formElements) {
        if (!element.name) continue;

        let value;
        switch (element.type) {
            case 'checkbox': value = element.checked.toString(); break;
            default: value = element.value; break;
        }
        formValues.set(element.name, value);
    }
    return formValues;
}

function copyToClipboard(text) {
    navigator.clipboard.writeText(text).then(() => {
        alert(`Copied ${text} to clipboard!`);
    }).catch(err => {
        console.error('Failed to copy password: ', err);
    });
}