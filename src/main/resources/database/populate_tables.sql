-- ==============================================================
--  Insert Init Data		                                        
-- ==============================================================

-- Ajout des fonctions
INSERT INTO fonction VALUES (1, 'FCTN_API_EDITION_RAPPORT');
INSERT INTO fonction VALUES (2, 'FCTN_GESTION_CPT_IMPUT');
INSERT INTO fonction VALUES (3, 'FCTN_API_CONFIG_APPLIS');
INSERT INTO fonction VALUES (4, 'FCTN_MANAGE_USERS');

-- Ajout des rôles
INSERT INTO role VALUES (1, 'ROLE_MANAGE');
INSERT INTO role VALUES (2, 'ROLE_REPORT');
INSERT INTO role VALUES (3, 'ROLE_SUPER_ADMIN');

-- Association des fonctions aux rôles
INSERT INTO role_composition VALUES (1, 2);
INSERT INTO role_composition VALUES (1, 3);
INSERT INTO role_composition VALUES (2, 1);
INSERT INTO role_composition VALUES (3, 1);
INSERT INTO role_composition VALUES (3, 2);
INSERT INTO role_composition VALUES (3, 3);
INSERT INTO role_composition VALUES (3, 4);

-- Ajout du premier super administrateur
INSERT INTO user_bo_smsu VALUES (1, 'admin', 3);

commit;