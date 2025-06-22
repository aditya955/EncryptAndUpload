# EncryptAndUpload

EncryptAndUpload is a Java application that allows you to encrypt files and upload them to cloud storage providers (currently Google Drive). 

## Features
- Encryption with configurable parameters (Currently: AES (128/256 bit))
- Upload encrypted files to Google Drive
- Modular and extensible design
- Command-line interface (CLI)

## Installation

### Prerequisites
- Java 17 or higher
- Maven

### Build
Clone the repository and build the project using Maven:
```sh
git clone https://github.com/aditya955/EncryptAndUpload.git
cd EncryptAndUpload
mvn clean package
```

## Usage

### Configuration
- [Follow this guide](https://developers.google.com/workspace/drive/api/quickstart/java#authorize_credentials_for_a_desktop_application) to create `credentials.json`.
- Place your Google Drive credentials in `local-config/credentials.json`.
- (Optional) Create `local-config/config.properties` to set up encryption and upload options. (Currently only upload options is supported)

```properties
# Example config.properties
upload.provider = GOOGLE_DRIVE
upload.applicationName = UploadTest
upload.authPort = 8888
upload.credsFilePath = local-config/credentials.json
upload.tokensDirPath = local-config
```

### Run the Application
```sh
java -jar target/EncryptAndUpload-1.0-SNAPSHOT.jar
```

### Example: Encrypt and Upload a File
#### Encrypt a file
- This encrypts `input.txt` using AES-256 and saves it as `output.enc`. It prompts for a password.
```sh
java -jar target/EncryptAndUpload-1.0-SNAPSHOT.jar encrypt --algorithm AES256 input.txt output.enc -p
```

- This encrypts `input.txt` using AES-128 and saves it as `output.enc`. It uses `password` as password to encrypt the file.
```sh
java -jar target/EncryptAndUpload-1.0-SNAPSHOT.jar encrypt --algorithm AES128 input.txt output.enc -p password
```

- This decrypts `input.enc` using AES-128 and saves it as `output.txt`. It uses `password` as password to decrypt the file.
```sh
java -jar target/EncryptAndUpload-1.0-SNAPSHOT.jar encrypt --decrypt --algorithm AES128 input.enc output.txt -p password
```

See `java -jar target/EncryptAndUpload-1.0-SNAPSHOT.jar encrypt --help` for more options and usage details.

#### Upload a file/Cloud Operations
- This lists all available files in google drive along with it's file name & file id.
```sh
java -jar target/EncryptAndUpload-1.0-SNAPSHOT.jar upload --provider GOOGLE_DRIVE --list
```

- This uploads the file `fileToUpload.enc` to Google Drive.
```sh
java -jar target/EncryptAndUpload-1.0-SNAPSHOT.jar upload --provider GOOGLE_DRIVE --upload fileToUpload.enc
```

- This downloads the given `file id` from Google Drive to `save location`.
```sh
java -jar target/EncryptAndUpload-1.0-SNAPSHOT.jar upload --provider GOOGLE_DRIVE --download <fileId> <saveLocation>
```

- (Using config.properties) This uses given config.properties file for configuration and lists all files in Google Drive.
```sh
java -jar target/EncryptAndUpload-1.0-SNAPSHOT.jar upload --config local-config/config.properties --list
```

See `java -jar target/EncryptAndUpload-1.0-SNAPSHOT.jar upload --help` for more options and usage details.
