package br.com.fiap.execution;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.importer.ClassFileImporter;
import org.junit.jupiter.api.Test;

class ArchitectureTest {
  @Test
  void domainAndApplicationAreIndependent() {
    var classes = new ClassFileImporter().importPackages("br.com.fiap.execution");
    noClasses()
        .that()
        .resideInAPackage("..domain..")
        .should()
        .dependOnClassesThat()
        .resideInAnyPackage(
            "..adapter..", "..application..", "io.quarkus..", "software.amazon..", "jakarta..")
        .check(classes);
    noClasses()
        .that()
        .resideInAPackage("..application..")
        .should()
        .dependOnClassesThat()
        .resideInAnyPackage("..adapter..", "software.amazon..")
        .check(classes);
  }
}
