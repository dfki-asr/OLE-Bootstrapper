FROM maven:3.3.9-jdk-8

# Download ldraw to /usr/share/ldraw

COPY download-ldraw-lib.sh /tmp/ldcad/download-ldraw-lib.sh
RUN chmod +x /tmp/ldcad/download-ldraw-lib.sh && \
    mkdir -p /usr/share && \
    cd /usr/share && \
    /tmp/ldcad/download-ldraw-lib.sh && \
    rm /tmp/ldcad/download-ldraw-lib.sh

ENV LDRAWDIR=/usr/share/ldraw

RUN mkdir -p /usr/src/ole-bootstrapper
WORKDIR /usr/src/ole-bootstrapper

ADD . /usr/src/ole-bootstrapper

ARG MAVEN_LOCAL_REPO=/usr/share/m2
ENV MAVEN_LOCAL_REPO "${MAVEN_LOCAL_REPO}"
RUN mkdir -p "$MAVEN_LOCAL_REPO"

RUN for d in deps/*; do \
      if [ -d "$d" ]; then \
        (cd "$d"; mvn -Dmaven.repo.local="$MAVEN_LOCAL_REPO" clean install) || exit 1; \
      fi; \
    done

RUN mvn -Dmaven.repo.local="$MAVEN_LOCAL_REPO" clean package install
CMD mvn -Dmaven.repo.local="$MAVEN_LOCAL_REPO" -Dexec.mainClass="de.dfki.resc28.ole.bootstrap.App" exec:java
