echo "[INFO] Building the project"

if [[ $1 == "-v" ]]
then
    mvn clean install
else
    mvn clean install -q
fi

if [ $? -ne 0 ]; then
    echo "[ERROR] Build failed"
    exit 1
fi

echo "[INFO] Running the project"
cd target/
# java -jar secureFileUpload-1.0.0.jar -i $INPUT_FILE -o $OUTPUT_FILE

echo -e "\n[INFO] Project execution completed"
cd ..