FROM ubuntu:latest

RUN apt-get -yqq update
RUN apt-get install -yqq nginx
RUN echo "\ndaemon off;" >> /etc/nginx/nginx.conf
COPY web.conf /etc/nginx/sites-available/
RUN ln /etc/nginx/sites-available/web.conf /etc/nginx/sites-enabled/web.conf
RUN rm /etc/nginx/sites-enabled/default /etc/nginx/sites-available/default
RUN mkdir /var/www/test/
RUN echo "Hello world!" > /var/www/test/index.html

WORKDIR /etc/nginx
EXPOSE 80
CMD ["nginx"]
