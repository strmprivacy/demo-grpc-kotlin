mkdir -p build
rm -r build/*
protoc *.proto --java_out=build
