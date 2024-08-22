function validateFileCount(input, maxFiles) {
    console.log("validating, " + maxFiles, input)
    const fileCount = input.files.length;
    const errorElement = document.getElementById('file-error');

    if (fileCount > maxFiles) {
        errorElement.style.display = 'block';
        input.value = ''; // Clear the input if the limit is exceeded
    } else {
        errorElement.style.display = 'none';
    }
}