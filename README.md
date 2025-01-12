# EncryptAndUpload
A CLI tool to encrypt and upload files to cloud (Currently only GDrive supported).

## Installation
### Getting repo
- Clone the repo
`git clone https://github.com/aditya955/EncryptAndUpload.git`
- Run `run.sh` to create jar file inside `target` directory.
- Execute .jar file using `java -jar <arguments>`

### Creating Google Drive API credentials
- [Enable the API](https://developers.google.com/drive/api/quickstart/java#enable_the_api)
- [Configure OAuth consent screen](https://developers.google.com/drive/api/quickstart/java#configure_the_oauth_consent_screen)
- [Authorize credentials](https://developers.google.com/drive/api/quickstart/java#authorize_credentials_for_a_desktop_application)

### Creating config file
- Create a `config.yaml` file in the src/main/resources/.
- Example yaml file
```yaml
applicationName: "Upload File"

credentialsFilePath: "credentials.json" # Google Drive API credentials file

tokensDirectoryPath: "/tmp/tokens" # Directory to store tokens

keySize: 256    # Key size for encryption
googleDriveAuthenticationPort: 8888 # Port for google drive authentication

# Google Drive scopes (depending on use case)
# If changing scopes, make sure to delete the tokens directory
driveScopes:
  - DRIVE_METADATA_READONLY
  - DRIVE_FILE
  - DRIVE
```

## Usage
- `java -jar EncryptAndUpload.jar -h` to get help.

### Basic Usage:

1. **Encrypt File:** 
`java -jar EncryptAndUpload.jar -e <file-path>`
This will encrypt the file with AES encryption algorithm and save it in the same directory with `.encrypted` extension. <br/>
Optionally `--encryptedFile` flag can be used for specifying the output file name.<br/>
This will print the key used for encryption, which can later be used for decryption.

<br />

2. **Upload File:**
`java -jar EncryptAndUpload.jar -u <file-path>`
This will upload the file to google cloud and print the file id.

<br />

3. **Download File:**
`java -jar EncryptAndUpload.jar -d <file-id>`
This will download the file from google cloud and save it in the same directory with `.downloaded` extension.<br/>
Optionally `--downloadedFile` flag can be used for specifying the output file name.

<br />

4. **Decrypt File:**
`java -jar EncryptAndUpload.jar -i <file-path> -k <key>`
This will decrypt the file with the given key and save it in the same directory with `.decrypted` extension.<br/>
`-k` or `--key` flag is optional as an argument, if not provied, it will be prompted during runtime.<br/>
Optionally `--decryptedFile` flag can be used for specifying the output file name.

Multiple flags can be used together for chaining the operations.
Operation sequence: `Encrypt -> Upload -> Download -> Decrypt`, operation is performed from left to right.