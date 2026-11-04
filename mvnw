#!/usr/bin/env sh
set -eu

MAVEN_VERSION=3.9.9
DIST_NAME="apache-maven-${MAVEN_VERSION}"
USER_HOME="${MAVEN_USER_HOME:-${HOME}/.m2}"
DIST_ROOT="${USER_HOME}/wrapper/dists"
MAVEN_HOME="${DIST_ROOT}/${DIST_NAME}"

if [ ! -x "${MAVEN_HOME}/bin/mvn" ]; then
  command -v curl >/dev/null 2>&1 || { printf 'curl is required to bootstrap Maven.\n' >&2; exit 1; }
  if ! command -v unzip >/dev/null 2>&1 && ! command -v jar >/dev/null 2>&1; then
    printf 'unzip or the JDK jar tool is required to bootstrap Maven.\n' >&2
    exit 1
  fi
  command -v sha512sum >/dev/null 2>&1 || { printf 'sha512sum is required to verify Maven.\n' >&2; exit 1; }
  mkdir -p "${DIST_ROOT}"
  archive="$(mktemp)"
  checksum="$(mktemp)"
  trap 'rm -f "$archive" "$checksum"' EXIT HUP INT TERM
  url="https://archive.apache.org/dist/maven/maven-3/${MAVEN_VERSION}/binaries/${DIST_NAME}-bin.zip"
  curl --fail --location --silent --show-error "${url}" -o "${archive}"
  curl --fail --location --silent --show-error "${url}.sha512" -o "${checksum}"
  expected="$(awk '{print $1}' "${checksum}")"
  printf '%s  %s\n' "${expected}" "${archive}" | sha512sum --check --status
  if command -v unzip >/dev/null 2>&1; then
    unzip -q "${archive}" -d "${DIST_ROOT}"
  else
    (cd "${DIST_ROOT}" && jar -xf "${archive}")
  fi
fi

exec "${MAVEN_HOME}/bin/mvn" "$@"