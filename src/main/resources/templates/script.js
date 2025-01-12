function generatePasswords() {
        const paramsObject = Object.fromEntries(getFormValues("passwordForm"));
        const url = "/api/";

        fetch(url, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(paramsObject),

        })
            .then(response => response.json())
            .then(data => {
                let passwords = [];
                if (data["passwords"]) passwords = data["passwords"];

                populatePasswordsTable(passwords);

                if (passwords) showElement('result');
            })
            .catch(error => {
                console.error('Error generating passwords:', error);
            });
}

function populatePasswordsTable(passwords) {
    const passwordsTable = document.getElementById('passwordsTable');
    passwordsTable.innerHTML = '';

    let set_background_color = false;
    const background_color = "#F3EEE1";

    passwords.forEach(password => {
        const row = document.createElement('tr');
        row.className = 'password_row';
        if (set_background_color) row.style.backgroundColor = background_color;
        set_background_color = !set_background_color;

        const password_cell = document.createElement('td');
        password_cell.className = 'password';
        password_cell.textContent = password;

        const copy_button_cell = document.createElement('td');
        const copy_button = document.createElement('button');
        copy_button.className = 'copy_button';
        copy_button.textContent = 'Copy';
        copy_button.onclick = () => copyToClipboard(password);
        copy_button_cell.appendChild(copy_button);

        row.appendChild(password_cell);
        row.appendChild(copy_button_cell);

        passwordsTable.appendChild(row);
    });
}

function setField(event) {
    const target = event.currentTarget;
    const param = target.getAttribute("param");
    const element = document.getElementById(param);
    const value = target.getAttribute("value");

    if (value === "true" || value === "false") element.checked = value === "true";
    else element.value = value;
}