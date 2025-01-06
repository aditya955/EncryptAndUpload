# EncryptAndUpload
A CLI tool to encrypt and upload files to cloud.

## Installation
- Clone the repo
`git clone https://github.com/aditya955/EncryptAndUpload.git`
- Run `run.sh` to create jar file inside `target` directory.
- Execute .jar file using `java -jar <arguments>`

### Usage
- `java -jar EncryptAndUpload.jar -h` to get help.

**Basic Usage:**

1. **Encrypt File:** 
`java -jar EncryptAndUpload.jar -e <file-path>`
This will encrypt the file with AES encryption algorithm and save it in the same directory with `.encrypted` extension.
Optionally `--encryptedFile` flag can be used for specifying the output file name.
This will print the key used for encryption, which can later be used for decryption.

<br />

2. **Upload File:**
`java -jar EncryptAndUpload.jar -u <file-path>`
This will upload the file to google cloud and print the file id.

<br />

3. **Download File:**
`java -jar EncryptAndUpload.jar -d <file-id>`
This will download the file from google cloud and save it in the same directory with `.downloaded` extension.
Optionally `--downloadedFile` flag can be used for specifying the output file name.

<br />

4. **Decrypt File:**
`java -jar EncryptAndUpload.jar -i <file-path> -k <key>`
This will decrypt the file with the given key and save it in the same directory with `.decrypted` extension.
`-k` or `--key` flag is optional as an argument, if not provied, it will be prompted during runtime.
Optionally `--decryptedFile` flag can be used for specifying the output file name.

Multiple flags can be used together for chaining the operations.
Operation sequence: `Encrypt -> Upload -> Download -> Decrypt`, operation is performed from left to right.