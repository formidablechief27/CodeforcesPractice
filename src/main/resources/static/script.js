function call() {
    document.getElementById("submitButton").addEventListener("click", function() {
        const checkboxes = document.getElementsByName("option");
        const selectedOptions = [];

        for (let checkbox of checkboxes) {
            if (checkbox.checked) {
                selectedOptions.push(checkbox.value);
            }
        }

        fetch('http://localhost:8080/tags', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(selectedOptions)
        })
		.then(response => {
		    console.log(response); // Print the response object
		    if (response.ok) {
		        return response.text(); // Parse response as text
		    } else {
		        throw new Error('Failed to send selected options');
		    }
		})
		.then(data => {
		    console.log(data); // Print the response data
		})
		.catch(error => {
        console.log('Error adding task:', error);
    	});
    });
}
