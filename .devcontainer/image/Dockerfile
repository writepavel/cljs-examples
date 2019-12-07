FROM node:12

RUN apt-get update \
  && apt-get install -y sudo zsh curl git \
  #
  # Add user `node` to `sudoers`
  && echo node ALL=\(root\) NOPASSWD:ALL > /etc/sudoers.d/node \
  && chmod 0440 /etc/sudoers.d/node
ENV SHELL /bin/zsh

# Set correct locale and install fonts-powerline (needed for agnoster theme in oh-my-zsh)
RUN apt-get install -y locales fonts-powerline rlwrap xdg-utils && \
  echo "Europe/Berlin" > /etc/timezone && \
  dpkg-reconfigure -f noninteractive tzdata && \
  sed -i -e 's/# en_US.UTF-8 UTF-8/en_US.UTF-8 UTF-8/' /etc/locale.gen && \
  echo 'LANG="en_US.UTF-8"'>/etc/default/locale && \
  dpkg-reconfigure --frontend=noninteractive locales && \
  update-locale LANG=en_US.UTF-8

ENV LANG en_US.UTF-8
ENV LANGUAGE en_US.UTF-8
ENV LC_ALL en_US.UTF-8

ARG javaVer=13.0.1
ARG javaDownloadLink=https://download.java.net/java/GA/jdk${javaVer}/cec27d702aa74d5a8630c65ae61e4305/9/GPL/openjdk-${javaVer}_linux-x64_bin.tar.gz
ARG javaDir=jdk-${javaVer}
ARG javaCACerts=lib/security/cacerts
RUN cd /tmp \
  && curl -Lo java.tar.gz ${javaDownloadLink} \
  && tar -C /opt -zxf java.tar.gz \
  && ln -s /opt/${javaDir}/bin/java /usr/local/bin/java \
  && chmod +x /usr/local/bin/java
# Patching CA certificates is required for some older Java versions
# RUN apt-get install -y ca-certificates-java \
#   && cp /etc/ssl/certs/java/cacerts /opt/${javaDir}/${javaCACerts}

RUN \
  curl -O https://download.clojure.org/install/linux-install-1.10.1.492.sh \
  && chmod +x linux-install-1.10.1.492.sh \
  && sudo ./linux-install-1.10.1.492.sh

RUN cd /tmp \
	&& curl -Lo /usr/local/bin/lein https://raw.githubusercontent.com/technomancy/leiningen/stable/bin/lein \
  && chmod +x /usr/local/bin/lein \
  && lein

USER node
RUN yarn global add lumo-cljs
RUN mkdir /home/node/bin && cd /home/node/bin \
  && echo '#!/usr/bin/zsh\nprint -P "%F{36}Link sent to default browser: %F{249}$@\u001b[39m"' > www-browser \
  && chmod +x *

# Install oh-my-zsh
RUN wget https://github.com/robbyrussell/oh-my-zsh/raw/master/tools/install.sh -O - | zsh || true \
  && git clone https://github.com/romkatv/powerlevel10k.git /home/node/.oh-my-zsh/custom/themes/powerlevel10k
COPY --chown=node:node ./config/* /home/node/

# Copy lifecycle scripts
COPY --chown=node:node scripts /home/node/scripts
RUN cd /home/node/scripts && chmod +x *

EXPOSE 9500 7888
