fn main() {
    println!("cargo:rerun-if-changed=proto/inference.proto");

    let protoc_path = protoc_bin_vendored::protoc_bin_path().expect("Failed to find protoc");
    // SAFETY: build script runs in a single process context for this crate.
    unsafe {
        std::env::set_var("PROTOC", protoc_path);
    }

    tonic_prost_build::compile_protos("proto/inference.proto").expect("Failed to compile protos");
}

