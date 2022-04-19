mkdir -p build
rm -rf build/*
protoc *.proto --java_out=build
