#
# For some reason stetho doesn't see to add this exclude themselves. The problem appeared with the new
# gradle (3.0 :: api/implementation) so it might be related to that.
#
-keep class org.apache.commons.cli.** {*; }
