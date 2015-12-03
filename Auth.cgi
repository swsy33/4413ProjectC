#!/usr/bin/perl -w
use CGI qw(:all);
use CGI::Carp qw(fatalsToBrowser);
use strict;
#################################
my $ip = $ENV{"REMOTE_ADDR"};
my $now = `date`;
my $usr = $ENV{"REMOTE_USER"};
my $rm = $ENV{"REQUEST_METHOD"};
my $back = "localhost:4413/mcPlusPlus2/Admin";
#param("back");
my $husr = `sha1sum <<< \$usr`;
my $ss = "shared secret";
my $hash = `sha1sum <<< "shared secret"`;


# username
my $indexStr = index($husr, ' ');
my $newStr = substr $husr, 0, $indexStr;

# password
$indexStr = index($hash, ' '); 
my $newStr2 = substr $hash, 0, $indexStr;

#my $url = "http://www.yorku.ca?user=$husr&hash=$hash";
my $url = "http://localhost:4413/mcPlusPlus2/Admin?user=$usr\&hash=$newStr2";
#my $url = $back;
print "Location: $url\n\n";
print "Content-type: text/html\n\n";
###############
#print "sdfsdfsd------$url\n<br/>";
print "$url<br/>";
print "husr------$husr<br/>";
print "hash------$hash<br/>";
print "the new output------$newStr<br/>";
print "the new output------$newStr2<br/>";


open(LOG, ">>log.txt") || die "Couldn't open the log";
print LOG "Request from $ip at $now.... user is: $usr and the method is: $rm\n parameter back is $back\n";
close(LOG);
