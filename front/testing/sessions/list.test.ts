import {ListComponent} from "../../src/app/features/sessions/components/list/list.component";
import {ComponentFixture, TestBed} from "@angular/core/testing";
import {HttpClientModule} from "@angular/common/http";
import {MatCardModule} from "@angular/material/card";
import {MatIconModule} from "@angular/material/icon";
import {SessionService} from "../../src/app/services/session.service";
import {expect} from "@jest/globals";
import {DebugElement} from "@angular/core";
import {By} from "@angular/platform-browser";
import {of} from "rxjs";
import {RouterTestingModule} from "@angular/router/testing";

/**
 * Test du composant ListComponent
 */
describe('Component: ListComponent', () => {

    // Déclaration des variables
    let component: ListComponent;
    let fixture: ComponentFixture<ListComponent>;
    let mockSessionService = {
        sessionInformation: {
            admin: true
        }
    }

    let listSessions = [
        {
            id: 1,
            name: 'Test',
            description: 'Test',
            date: new Date(),
            teacher_id: 1,
            users: [],
            createdAt: new Date(),
            updatedAt: new Date()
        }
    ];
    let debugElement: DebugElement;

    // Avant chaque test on configure le composant et on récupère les services
    beforeEach(async () => {
        await TestBed.configureTestingModule({
            declarations: [ListComponent],
            imports: [HttpClientModule, MatCardModule, MatIconModule, RouterTestingModule],
            providers: [{provide: SessionService, useValue: mockSessionService}]
        })
            .compileComponents();

        fixture = TestBed.createComponent(ListComponent);
        component = fixture.componentInstance;
        // Mock the sessions$ observable to emit test data
        component.sessions$ = of(listSessions);
        debugElement = fixture.debugElement;
        fixture.detectChanges();
    });

    // Test de l'affichage des sessions
    it('should list all sessions', () => {
        const user = component.user;

        expect(user).toBeDefined();
        expect(component.sessions$).toBeDefined();
        expect(component.sessions$).not.toBe([]);
    });

    // Test de l'affichage des boutons de création et d'édition de session
    it('should display the create | edit session buttons', () => {
        const user = component.user;
        const buttons: DebugElement[] = debugElement.queryAll(By.css('button'));

        expect(user).toBeDefined();
        expect(buttons).toBeDefined();
        expect(buttons.length).toBe(3);
    });

    // Test de la dissimulation des boutons de création et d'édition de session
    it('should not display the create | edit session button', () => {
        mockSessionService.sessionInformation.admin = false;
        fixture.detectChanges();

        const user = component.user;
        const buttons: DebugElement[] = debugElement.queryAll(By.css('button'));

        expect(user).toBeDefined();
        expect(buttons).toBeDefined();
        expect(buttons.length).toBe(1);
    });
});